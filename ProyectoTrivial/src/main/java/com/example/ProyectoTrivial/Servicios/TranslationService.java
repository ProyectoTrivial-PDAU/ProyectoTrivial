package com.example.ProyectoTrivial.Servicios;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Servicio de traducción para pasar EN -> ES.
 * - Proveedor principal: LibreTranslate (autoalojado)
 * - Fallback opcional: MyMemory
 * - Caché en memoria para evitar traducciones repetidas
 */
@Service
public class TranslationService {

    private static final String MYMEMORY_API_URL = "https://api.mymemory.translated.net/get";
    private static final String LANG_PAIR = "en|es";
    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("__TK_(\\\\d+)__");
    private static final Pattern TOKEN_PATTERN = Pattern.compile(
            "(\\\\?:|::|==|!=|<=|>=|\\\\+\\\\+|--|=>|->|\\\\bC\\\\+\\\\+\\\\b|\\\\bC#\\\\b|\\\\b[A-Z]{2,10}\\\\b|\\\\b[a-zA-Z]+\\\\.[a-zA-Z0-9_]+\\\\b)");

    /**
     * Caché de traducciones: texto en inglés → texto en español.
     * ConcurrentHashMap para seguridad en hilos.
     */
    private final Map<String, String> cache = new ConcurrentHashMap<>();

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final String libreBaseUrl;
    private final String libreApiKey;
    private final boolean useMyMemoryFallback;

    public TranslationService(
            RestTemplate restTemplate,
            @Value("${app.translation.libre.base-url:http://localhost:5001}") String libreBaseUrl,
            @Value("${app.translation.libre.api-key:}") String libreApiKey,
            @Value("${app.translation.use-mymemory-fallback:true}") boolean useMyMemoryFallback) {
        this.restTemplate = restTemplate;
        this.objectMapper = new ObjectMapper();
        this.libreBaseUrl = libreBaseUrl;
        this.libreApiKey = libreApiKey;
        this.useMyMemoryFallback = useMyMemoryFallback;
    }

    /**
     * Traduce un solo texto de inglés a español.
     * Usa caché para evitar llamadas repetidas.
     *
     * @param text Texto en inglés
     * @return Texto traducido al español, o el original si falla
     */
    public String translate(String text) {
        if (text == null || text.isBlank()) return text;

        if (shouldSkipTranslation(text)) {
            return text;
        }

        // Revisar caché
        String cached = cache.get(text);
        if (cached != null) return cached;

        try {
            ProtectedText protectedText = protectTokens(text);
            String translated = translateWithLibre(protectedText.protectedInput());

            if ((translated == null || translated.isBlank()) && useMyMemoryFallback) {
                translated = translateWithMyMemory(protectedText.protectedInput());
            }

            if (translated != null && !translated.isBlank()) {
                translated = restoreTokens(cleanTranslatedText(translated), protectedText.tokenMap());
                translated = cleanTranslatedText(translated);
                if (!translated.isBlank()) {
                    cache.put(text, translated);
                    return translated;
                }
            }
        } catch (Exception e) {
            System.err.println("Error traduciendo texto: " + e.getMessage());
        }

        return text; // Devolver original si falla
    }

    public List<String> translateBatch(List<String> texts) {
        if (texts == null || texts.isEmpty()) return List.of();
        List<String> results = new ArrayList<>(texts.size());
        for (String text : texts) {
            results.add(translate(text));
        }
        return results;
    }

    private String translateWithLibre(String input) {
        try {
            String url = libreBaseUrl + "/translate";

            Map<String, Object> payload = new HashMap<>();
            payload.put("q", input);
            payload.put("source", "en");
            payload.put("target", "es");
            payload.put("format", "text");
            if (libreApiKey != null && !libreApiKey.isBlank()) {
                payload.put("api_key", libreApiKey);
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(payload, headers);

            String response = restTemplate.postForObject(url, entity, String.class);
            JsonNode root = objectMapper.readTree(response);
            return root.path("translatedText").asText();
        } catch (Exception e) {
            System.err.println("LibreTranslate no disponible, se usa fallback: " + e.getMessage());
            return null;
        }
    }

    private String translateWithMyMemory(String input) {
        try {
            String encoded = URLEncoder.encode(input, StandardCharsets.UTF_8);
            String url = MYMEMORY_API_URL + "?q=" + encoded + "&langpair=" + LANG_PAIR + "&de=brainiak@fake.com";
            String response = restTemplate.getForObject(url, String.class);
            JsonNode root = objectMapper.readTree(response);
            return root.path("responseData").path("translatedText").asText();
        } catch (Exception e) {
            System.err.println("Fallback MyMemory fallido: " + e.getMessage());
            return null;
        }
    }

    private boolean shouldSkipTranslation(String text) {
        String trimmed = text.trim();
        if (trimmed.matches("^[\\\\p{Punct}\\\\d\\\\s]+$")) return true;
        if (trimmed.length() <= 4 && trimmed.matches("^[A-Z0-9_+#:/?=-]+$")) return true;
        return trimmed.matches("^[A-Za-z0-9_+#:/?=<>!&*().,;\\\\-]+$") && !trimmed.contains(" ") && trimmed.length() <= 3;
    }

    private ProtectedText protectTokens(String text) {
        Map<String, String> tokenMap = new HashMap<>();
        Matcher matcher = TOKEN_PATTERN.matcher(text);
        StringBuffer sb = new StringBuffer();
        int index = 0;

        while (matcher.find()) {
            String key = "__TK_" + index + "__";
            tokenMap.put(key, matcher.group());
            matcher.appendReplacement(sb, Matcher.quoteReplacement(key));
            index++;
        }
        matcher.appendTail(sb);
        return new ProtectedText(sb.toString(), tokenMap);
    }

    private String restoreTokens(String translated, Map<String, String> tokenMap) {
        String restored = translated;
        for (Map.Entry<String, String> entry : tokenMap.entrySet()) {
            restored = restored.replace(entry.getKey(), entry.getValue());
        }
        return restored;
    }

    /**
     * Devuelve el tamaño actual de la caché (para diagnóstico).
     */
    public int getCacheSize() {
        return cache.size();
    }

    private String cleanTranslatedText(String raw) {
        if (raw == null) return null;

        String translated = URLDecoder.decode(raw, StandardCharsets.UTF_8);
        translated = translated.replaceAll("<x\\b[^>]*/>", "");
        translated = translated.replaceAll("</?x\\b[^>]*>", "");
        translated = translated.replaceAll("<[^>]*>", "");

        translated = translated
                .replace("&quot;", "\"")
                .replace("&#39;", "'")
                .replace("&apos;", "'")
                .replace("&amp;", "&")
                .replace("&lt;", "<")
                .replace("&gt;", ">")
                .replace("&nbsp;", " ");

        translated = translated.replaceAll("(?i)Comment", "");
        translated = translated.replaceAll("\\s+", " ").trim();
        translated = translated.replaceAll("\\s+([\\?!¡¿,.;:])", "$1");

        Matcher matcher = PLACEHOLDER_PATTERN.matcher(translated);
        if (matcher.find()) {
            // Dejar placeholders intactos; se restauran fuera.
            translated = translated.replaceAll("\\s+", " ").trim();
        }

        return translated.isBlank() ? raw : translated;
    }

    private record ProtectedText(String protectedInput, Map<String, String> tokenMap) {}
}
