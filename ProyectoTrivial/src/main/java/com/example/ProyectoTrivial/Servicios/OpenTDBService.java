package com.example.ProyectoTrivial.Servicios;

import com.example.ProyectoTrivial.DTO.OpenTDBQuestion;
import com.example.ProyectoTrivial.DTO.OpenTDBResponse;
import com.example.ProyectoTrivial.DTO.PreguntaDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Servicio que conecta con la API externa Open Trivia Database (opentdb.com).
 * Ofrece miles de preguntas de trivia en múltiples categorías.
 * Las preguntas se traducen automáticamente al español usando TranslationService.
 * 
 * API docs: https://opentdb.com/api_config.php
 * - No requiere autenticación
 * - type=multiple → preguntas de opción múltiple (4 opciones)
 * - amount=N → cantidad de preguntas
 * - category=ID → filtrar por categoría
 */
@Service
public class OpenTDBService {

    private static final String BASE_URL = "https://opentdb.com/api.php";

    private final RestTemplate restTemplate;
    private final TranslationService translationService;

    /**
     * Mapa de categorías en español → ID de OpenTDB.
     * Cada ID corresponde a una categoría real de la API.
     * Ref: https://opentdb.com/api_category.php
     */
    private static final Map<String, Integer> CATEGORY_MAP = new LinkedHashMap<>();
    static {
        CATEGORY_MAP.put("Conocimiento General", 9);
        CATEGORY_MAP.put("Ciencia y Naturaleza", 17);
        CATEGORY_MAP.put("Ciencia: Computación", 18);
        CATEGORY_MAP.put("Matemáticas", 19);
        CATEGORY_MAP.put("Geografía", 22);
        CATEGORY_MAP.put("Historia", 23);
        CATEGORY_MAP.put("Arte", 25);
        CATEGORY_MAP.put("Deportes", 21);
        CATEGORY_MAP.put("Cine", 11);
        CATEGORY_MAP.put("Música", 12);
        CATEGORY_MAP.put("Videojuegos", 15);
        CATEGORY_MAP.put("Animación y Manga", 31);
        CATEGORY_MAP.put("Animales", 27);
        CATEGORY_MAP.put("Mitología", 20);
        CATEGORY_MAP.put("Vehículos", 28);
        CATEGORY_MAP.put("Política", 24);
        CATEGORY_MAP.put("Celebridades", 26);
        CATEGORY_MAP.put("Cómics", 29);
        CATEGORY_MAP.put("Gadgets", 30);
    }

    public OpenTDBService(RestTemplate restTemplate, TranslationService translationService) {
        this.restTemplate = restTemplate;
        this.translationService = translationService;
    }

    /**
     * Devuelve la lista de categorías disponibles (nombres en español).
     */
    public List<String> getCategorias() {
        return new ArrayList<>(CATEGORY_MAP.keySet());
    }

    /**
     * Obtiene preguntas de OpenTDB, opcionalmente filtradas por categoría.
     * Si la categoría no coincide con ninguna del mapa, devuelve preguntas aleatorias.
     * 
     * @param cantidad  Número de preguntas a obtener (máx 50 por llamada de OpenTDB)
     * @param categoria Nombre de la categoría en español (o null para aleatorio)
     * @return Lista de PreguntaDTO listos para enviar al frontend (traducidos al español)
     */
    public List<PreguntaDTO> getPreguntas(int cantidad, String categoria) {
        // Construir URL
        StringBuilder url = new StringBuilder(BASE_URL);
        url.append("?amount=").append(Math.min(cantidad, 50));
        url.append("&type=multiple"); // Solo opción múltiple (4 opciones)

        // Buscar ID de categoría
        if (categoria != null && !categoria.isBlank()) {
            Integer catId = CATEGORY_MAP.get(categoria);
            if (catId != null) {
                url.append("&category=").append(catId);
            }
        }

        try {
            OpenTDBResponse response = restTemplate.getForObject(url.toString(), OpenTDBResponse.class);

            if (response == null || response.getResponse_code() != 0 || response.getResults() == null) {
                return List.of();
            }

            // 1. Mapear a DTOs (decodificar HTML, mezclar opciones)
            List<PreguntaDTO> dtos = response.getResults().stream()
                    .map(q -> mapToDTO(q, categoria))
                    .collect(Collectors.toList());

            // 2. Traducir todas las preguntas y opciones al español
            return translatePreguntas(dtos);

        } catch (Exception e) {
            System.err.println("Error llamando a OpenTDB: " + e.getMessage());
            return List.of();
        }
    }

    /**
     * Traduce una lista de PreguntaDTO al español.
     * Traduce cada texto individualmente para evitar problemas con separadores.
     * El servicio de traducción usa caché internamente para no repetir llamadas.
     */
    private List<PreguntaDTO> translatePreguntas(List<PreguntaDTO> dtos) {
        for (PreguntaDTO dto : dtos) {
            try {
                String originalCorrect = dto.getRespuesta_correcta();
                List<String> originalOptions = new ArrayList<>(dto.getOpciones());

                dto.setPregunta(sanitizeForDisplay(translationService.translate(dto.getPregunta())));

                List<String> translatedOptions = new ArrayList<>();
                for (String option : originalOptions) {
                    translatedOptions.add(sanitizeForDisplay(translationService.translate(option)));
                }

                List<String> safeOptions = buildSafeOptions(translatedOptions, originalOptions, originalCorrect);
                dto.setOpciones(safeOptions);

                String translatedCorrect = sanitizeForDisplay(translationService.translate(originalCorrect));
                String resolvedCorrect = resolveCorrectAnswer(translatedCorrect, originalCorrect, originalOptions, safeOptions);
                dto.setRespuesta_correcta(resolvedCorrect);
            } catch (Exception e) {
                System.err.println("Error traduciendo pregunta, se mantiene en inglés: " + e.getMessage());
            }
        }
        return dtos;
    }

    /**
     * Convierte una pregunta de OpenTDB a nuestro PreguntaDTO.
     * - Decodifica HTML entities
     * - Mezcla las opciones aleatoriamente
     * - Traduce el nombre de categoría al español si es posible
     */
    private PreguntaDTO mapToDTO(OpenTDBQuestion q, String categoriaOriginal) {
        PreguntaDTO dto = new PreguntaDTO();

        // Usar nombre de categoría en español si se proporcionó, sino traducir de OpenTDB
        String catName = categoriaOriginal != null && !categoriaOriginal.isBlank()
                ? categoriaOriginal
                : translateCategory(q.getCategory());
        dto.setCategoria(sanitizeForDisplay(catName));

        // Decodificar HTML entities del texto
        dto.setPregunta(sanitizeForDisplay(decodeHtml(q.getQuestion())));
        dto.setRespuesta_correcta(sanitizeForDisplay(decodeHtml(q.getCorrect_answer())));

        // Mezclar opciones: correcta + incorrectas
        List<String> opciones = new ArrayList<>();
        opciones.add(sanitizeForDisplay(decodeHtml(q.getCorrect_answer())));
        if (q.getIncorrect_answers() != null) {
            q.getIncorrect_answers().forEach(a -> opciones.add(sanitizeForDisplay(decodeHtml(a))));
        }
        Collections.shuffle(opciones);
        dto.setOpciones(opciones);

        return dto;
    }

    /**
     * Intenta traducir el nombre de categoría de OpenTDB (inglés) al español.
     * Si no hay traducción, devuelve el nombre original.
     */
    private String translateCategory(String englishCategory) {
        if (englishCategory == null) return "General";

        // Decodificar HTML entities en el nombre de categoría
        englishCategory = decodeHtml(englishCategory);

        // Buscar en nuestro mapa inverso
        for (Map.Entry<String, Integer> entry : CATEGORY_MAP.entrySet()) {
            // Comparación aproximada
            String eng = englishCategory.toLowerCase();
            String esp = entry.getKey().toLowerCase();
            if (eng.contains("general") && esp.contains("general")) return entry.getKey();
            if (eng.contains("science") && eng.contains("nature") && esp.contains("naturaleza")) return entry.getKey();
            if (eng.contains("science") && eng.contains("computer") && esp.contains("computación")) return entry.getKey();
            if (eng.contains("mathematics") && esp.contains("matemáticas")) return entry.getKey();
            if (eng.contains("geography") && esp.contains("geografía")) return entry.getKey();
            if (eng.contains("history") && esp.contains("historia")) return entry.getKey();
            if (eng.contains("art") && !eng.contains("entertainment") && esp.contains("arte")) return entry.getKey();
            if (eng.contains("sports") && esp.contains("deportes")) return entry.getKey();
            if (eng.contains("film") && esp.contains("cine")) return entry.getKey();
            if (eng.contains("music") && esp.contains("música")) return entry.getKey();
            if (eng.contains("video games") && esp.contains("videojuegos")) return entry.getKey();
            if ((eng.contains("anime") || eng.contains("cartoon") || eng.contains("animation")) && esp.contains("animación")) return entry.getKey();
            if (eng.contains("animals") && esp.contains("animales")) return entry.getKey();
            if (eng.contains("mythology") && esp.contains("mitología")) return entry.getKey();
            if (eng.contains("vehicles") && esp.contains("vehículos")) return entry.getKey();
            if (eng.contains("politics") && esp.contains("política")) return entry.getKey();
            if (eng.contains("celebrities") && esp.contains("celebridades")) return entry.getKey();
            if (eng.contains("comics") && esp.contains("cómics")) return entry.getKey();
            if (eng.contains("gadgets") && esp.contains("gadgets")) return entry.getKey();
            if (eng.contains("television") && esp.contains("cine")) return entry.getKey();
            if (eng.contains("board games") && esp.contains("general")) return entry.getKey();
            if (eng.contains("books") && esp.contains("arte")) return entry.getKey();
        }
        return englishCategory; // Devolver original si no se encuentra traducción
    }

    /**
     * Decodifica HTML entities comunes que devuelve OpenTDB.
     * Ej: &amp;quot; → ", &amp;amp; → &, &amp;#039; → ', etc.
     */
    private String decodeHtml(String text) {
        if (text == null) return null;
        return text
                .replace("&quot;", "\"")
                .replace("&amp;", "&")
                .replace("&lt;", "<")
                .replace("&gt;", ">")
                .replace("&#039;", "'")
                .replace("&apos;", "'")
                .replace("&ldquo;", "\u201C")
                .replace("&rdquo;", "\u201D")
                .replace("&lsquo;", "\u2018")
                .replace("&rsquo;", "\u2019")
                .replace("&eacute;", "é")
                .replace("&Eacute;", "É")
                .replace("&ntilde;", "ñ")
                .replace("&uuml;", "ü");
    }

    private List<String> buildSafeOptions(List<String> translatedOptions, List<String> originalOptions, String originalCorrect) {
        LinkedHashSet<String> candidateSet = new LinkedHashSet<>();

        for (String option : translatedOptions) {
            String cleaned = sanitizeForDisplay(option);
            if (!isInvalidOption(cleaned)) {
                candidateSet.add(cleaned);
            }
        }

        for (String option : originalOptions) {
            String cleaned = sanitizeForDisplay(option);
            if (!isInvalidOption(cleaned)) {
                candidateSet.add(cleaned);
            }
        }

        List<String> safe = new ArrayList<>(candidateSet);
        String cleanedCorrect = sanitizeForDisplay(originalCorrect);

        // La respuesta correcta siempre debe estar incluida.
        if (!containsNormalized(safe, cleanedCorrect)) {
            safe.add(0, cleanedCorrect);
        }

        // Si hay más de 4, recortar sin eliminar la correcta.
        if (safe.size() > 4) {
            List<String> trimmed = new ArrayList<>();
            trimmed.add(getCanonicalOrDefault(safe, cleanedCorrect));
            for (String option : safe) {
                if (trimmed.size() == 4) break;
                if (!containsNormalized(trimmed, option)) {
                    trimmed.add(option);
                }
            }
            safe = trimmed;
        }

        // Si por cualquier motivo hay menos de 4, completar con distractores neutros.
        List<String> fillers = List.of("Ninguna de las anteriores", "No aplica", "No estoy seguro", "No lo se");
        for (String filler : fillers) {
            if (safe.size() == 4) break;
            if (!containsNormalized(safe, filler)) {
                safe.add(filler);
            }
        }

        // Ultimo guardarrail: devolver exactamente 4 elementos.
        if (safe.size() > 4) {
            safe = new ArrayList<>(safe.subList(0, 4));
        }

        return safe;
    }

    private String resolveCorrectAnswer(
            String translatedCorrect,
            String originalCorrect,
            List<String> originalOptions,
            List<String> safeOptions) {

        if (!isInvalidOption(translatedCorrect) && containsNormalized(safeOptions, translatedCorrect)) {
            return getCanonicalFromOptions(safeOptions, translatedCorrect);
        }

        int originalCorrectIdx = originalOptions.indexOf(originalCorrect);
        if (originalCorrectIdx >= 0 && originalCorrectIdx < safeOptions.size()) {
            return safeOptions.get(originalCorrectIdx);
        }

        if (containsNormalized(safeOptions, originalCorrect)) {
            return getCanonicalFromOptions(safeOptions, originalCorrect);
        }

        return safeOptions.isEmpty() ? sanitizeForDisplay(originalCorrect) : safeOptions.get(0);
    }

    private boolean containsNormalized(List<String> options, String target) {
        String normalizedTarget = normalize(target);
        for (String option : options) {
            if (normalize(option).equals(normalizedTarget)) {
                return true;
            }
        }
        return false;
    }

    private String getCanonicalFromOptions(List<String> options, String target) {
        String normalizedTarget = normalize(target);
        for (String option : options) {
            if (normalize(option).equals(normalizedTarget)) {
                return option;
            }
        }
        return target;
    }

    private String getCanonicalOrDefault(List<String> options, String target) {
        String canonical = getCanonicalFromOptions(options, target);
        return isInvalidOption(canonical) ? sanitizeForDisplay(target) : canonical;
    }

    private String normalize(String text) {
        return text == null ? "" : text.trim().toLowerCase(Locale.ROOT);
    }

    private boolean isInvalidOption(String text) {
        if (text == null || text.isBlank()) return true;
        String t = text.trim();
        if (t.equals("?")) return true;
        return t.matches("^[\\p{Punct}\\s]+$");
    }

    /**
     * Limpieza final para UI sin tocar semántica de textos técnicos.
     */
    private String sanitizeForDisplay(String text) {
        if (text == null || text.isBlank()) return text;

        String cleaned = text
                .replaceAll("<x\\b[^>]*/>", "")
                .replaceAll("</?x\\b[^>]*>", "")
                .replaceAll("<[^>]+>", "")
                .replaceAll("(?i)Comment", "")
                .replace("&quot;", "\"")
                .replace("&#39;", "'")
                .replace("&apos;", "'")
                .replace("&amp;", "&")
                .replace("&lt;", "<")
                .replace("&gt;", ">")
                .replace("&nbsp;", " ")
                .replaceAll("\\s+", " ")
                .trim();
        cleaned = cleaned.replaceAll("\\s+([\\?!¡¿,.;:])", "$1");

        return cleaned.isBlank() ? text : cleaned;
    }
}
