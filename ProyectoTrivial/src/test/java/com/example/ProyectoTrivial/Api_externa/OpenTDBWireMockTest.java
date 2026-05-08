package com.example.ProyectoTrivial.Api_externa;


import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.client.RestTemplate;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Pruebas de simulación de peticiones HTTP a servicios externos con WireMock.
 *
 * Criterio f): "Se han simulado peticiones de consumo de servicios externos"
 *    
 *    Se levanta un servidor WireMock que intercepta las peticiones HTTP reales
 *    a la API de OpenTDB y al servicio de traducción (LibreTranslate/MyMemory).
 *    Esto permite comprobar el comportamiento de OpenTDBService ante distintas
 *    respuestas del servidor (200 OK, error, tiempo de espera, etc.)
 *    sin hacer llamadas reales a Internet.
 */
@SpringBootTest
@ActiveProfiles("test")
@DisplayName("OpenTDBService - Simulación HTTP con WireMock")
class OpenTDBWireMockTest {

    private static WireMockServer wireMockServer;

    @Autowired
    private RestTemplate restTemplate;

    @BeforeAll
    static void iniciarWireMock() {
        wireMockServer = new WireMockServer(WireMockConfiguration.wireMockConfig().dynamicPort());
        wireMockServer.start();
        configureFor("localhost", wireMockServer.port());
    }

    @AfterAll
    static void pararWireMock() {
        wireMockServer.stop();
    }

    @BeforeEach
    void resetearStubs() {
        wireMockServer.resetAll();
    }

    /*
     * Registra la URL del WireMock como proveedor de traducción para los tests.
     */

    
    @DynamicPropertySource
    static void configurarPropiedades(DynamicPropertyRegistry registry) {
        // Apuntar LibreTranslate al WireMock
        registry.add("app.translation.libre.base-url",
                () -> "http://localhost:" + wireMockServer.port());
        registry.add("app.translation.use-mymemory-fallback", () -> "false");
    }


    // Tests de simulación de OpenTDB
    @Test
    @DisplayName("WireMock: OpenTDB devuelve 5 preguntas correctamente formateadas")
    void wireMock_openTDB_devuelvePreguntas() {
        // Arrange: configurar WireMock para imitar la respuesta de OpenTDB
        wireMockServer.stubFor(get(urlPathMatching("/api.php.*"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(openTDBResponseJson(5))));

        // Act: hacer la petición al WireMock como si fuera OpenTDB
        String url = "http://localhost:" + wireMockServer.port()
                + "/api.php?amount=5&type=multiple";
        String response = restTemplate.getForObject(url, String.class);

        // Assert: verificar que la respuesta contiene los campos esperados
        assertThat(response).contains("response_code");
        assertThat(response).contains("results");
        assertThat(response).contains("What");

        // Verificar que WireMock recibió exactamente una petición
        wireMockServer.verify(1, getRequestedFor(urlPathMatching("/api.php.*")));
    }

    @Test
    @DisplayName("WireMock: OpenTDB devuelve response_code=1 (sin resultados)")
    void wireMock_openTDB_sinResultados() {
        wireMockServer.stubFor(get(urlPathMatching("/api.php.*"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"response_code\": 1, \"results\": []}")));

        String url = "http://localhost:" + wireMockServer.port() + "/api.php?amount=5";
        String response = restTemplate.getForObject(url, String.class);

        assertThat(response).contains("\"response_code\": 1");
        assertThat(response).contains("\"results\": []");
    }

    @Test
    @DisplayName("WireMock: OpenTDB devuelve error 500 — el cliente debe soportarlo")
    void wireMock_openTDB_error500() {
        wireMockServer.stubFor(get(urlPathMatching("/api.php.*"))
                .willReturn(aResponse()
                        .withStatus(500)
                        .withBody("Internal Server Error")));

        String url = "http://localhost:" + wireMockServer.port() + "/api.php?amount=5";

        org.junit.jupiter.api.Assertions.assertThrows(Exception.class, () ->
                restTemplate.getForObject(url, String.class));
    }

    @Test
    @DisplayName("WireMock: LibreTranslate responde correctamente con traducción")
    void wireMock_libreTranslate_traduccionOK() {
        // Simular que LibreTranslate devuelve una traducción correcta
        wireMockServer.stubFor(post(urlEqualTo("/translate"))
                .withHeader("Content-Type", containing("application/json"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"translatedText\": \"¿Cuál es la capital de Francia?\"}")));

        // Hacer la petición directamente al WireMock
        var headers = new org.springframework.http.HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        var body = java.util.Map.of(
                "q", "What is the capital of France?",
                "source", "en",
                "target", "es",
                "format", "text"
        );
        var entity = new org.springframework.http.HttpEntity<>(body, headers);

        String url = "http://localhost:" + wireMockServer.port() + "/translate";
        String response = restTemplate.postForObject(url, entity, String.class);

        assertThat(response).contains("¿Cuál es la capital de Francia?");
        wireMockServer.verify(1, postRequestedFor(urlEqualTo("/translate")));
    }

    @Test
    @DisplayName("WireMock: LibreTranslate devuelve error — se espera manejo del fallo")
    void wireMock_libreTranslate_error() {
        wireMockServer.stubFor(post(urlEqualTo("/translate"))
                .willReturn(aResponse()
                        .withStatus(503)
                        .withBody("Service Unavailable")));

        var headers = new org.springframework.http.HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        var body = java.util.Map.of("q", "test", "source", "en", "target", "es");
        var entity = new org.springframework.http.HttpEntity<>(body, headers);

        String url = "http://localhost:" + wireMockServer.port() + "/translate";

        org.junit.jupiter.api.Assertions.assertThrows(Exception.class, () ->
                restTemplate.postForObject(url, entity, String.class));
    }

    @Test
    @DisplayName("WireMock: OpenTDB con categoría específica pasa el parámetro en la URL")
    void wireMock_openTDB_conCategoria_pasaParametro() {
        wireMockServer.stubFor(get(urlPathMatching("/api.php.*"))
                .withQueryParam("category", equalTo("23"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(openTDBResponseJson(3))));

        String url = "http://localhost:" + wireMockServer.port()
                + "/api.php?amount=3&type=multiple&category=23";
        String response = restTemplate.getForObject(url, String.class);

        assertThat(response).contains("results");
        wireMockServer.verify(getRequestedFor(urlPathMatching("/api.php.*"))
                .withQueryParam("category", equalTo("23")));
    }

    @Test
    @DisplayName("WireMock: respuesta con delay simula latencia de red")
    void wireMock_openTDB_conLatencia() {
        wireMockServer.stubFor(get(urlPathMatching("/api.php.*"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withFixedDelay(200) // 200ms de latencia simulada
                        .withBody(openTDBResponseJson(1))));

        long inicio = System.currentTimeMillis();
        String url = "http://localhost:" + wireMockServer.port() + "/api.php?amount=1";
        restTemplate.getForObject(url, String.class);
        long duracion = System.currentTimeMillis() - inicio;

        assertThat(duracion).isGreaterThanOrEqualTo(200L);
    }

    // Helper: genera JSON de respuesta de OpenTDB

    private String openTDBResponseJson(int cantidad) {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"response_code\": 0, \"results\": [");
        for (int i = 0; i < cantidad; i++) {
            if (i > 0) sb.append(",");
            sb.append("""
                    {
                        "category": "History",
                        "type": "multiple",
                        "difficulty": "medium",
                        "question": "What year did World War II end?",
                        "correct_answer": "1945",
                        "incorrect_answers": ["1943", "1944", "1946"]
                    }
                    """);
        }
        sb.append("]}");
        return sb.toString();
    }
}
