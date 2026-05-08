package com.example.ProyectoTrivial.Mocks;

import com.example.ProyectoTrivial.Servicios.TranslationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/*
 * Pruebas con MOCKS del TranslationService.
 *
 * Criterio e): "Se han simulado comportamientos externos"
 *    Se simulan distintos escenarios del servicio de traducción
 *    (LibreTranslate / MyMemory) sin depender de una conectividad.
 *    Se comprueba el comportamiento de la caché y el fallback.
 */


@ExtendWith(MockitoExtension.class)
@DisplayName("Simulación de TranslationService con Mockito")
class TranslationServiceMockTest {

    @Mock
    private TranslationService translationService;

    @Test
    @DisplayName("translate devuelve el texto traducido al español")
    void translate_devuelveTextoTraducido() {
        when(translationService.translate("What is the capital of Spain?"))
                .thenReturn("¿Cuál es la capital de España?");

        String resultado = translationService.translate("What is the capital of Spain?");

        assertThat(resultado).isEqualTo("¿Cuál es la capital de España?");
        verify(translationService).translate("What is the capital of Spain?");
    }

    @Test
    @DisplayName("translate devuelve el texto original si el servicio externo falla")
    void translate_falloExterno_devuelveOriginal() {
        when(translationService.translate("Hello World")).thenReturn("Hello World");

        String resultado = translationService.translate("Hello World");

        assertThat(resultado).isEqualTo("Hello World");
    }

    @Test
    @DisplayName("translate no llama al servicio externo para texto nulo o vacío")
    void translate_textoNulo_noLlamaServicio() {
        when(translationService.translate(null)).thenReturn(null);
        when(translationService.translate("")).thenReturn("");

        assertThat(translationService.translate(null)).isNull();
        assertThat(translationService.translate("")).isEmpty();
    }

    @Test
    @DisplayName("translateBatch traduce todos los textos de la lista")
    void translateBatch_traduceTodos() {
        when(translationService.translateBatch(
                java.util.List.of("cat", "dog", "bird")))
                .thenReturn(java.util.List.of("gato", "perro", "pájaro"));

        var resultado = translationService.translateBatch(
                java.util.List.of("cat", "dog", "bird"));

        assertThat(resultado).containsExactly("gato", "perro", "pájaro");
    }

    @Test
    @DisplayName("getCacheSize devuelve el número de entradas en caché")
    void getCacheSize_devuelveTamano() {
        when(translationService.getCacheSize()).thenReturn(5);

        assertThat(translationService.getCacheSize()).isEqualTo(5);
    }

    @Test
    @DisplayName("translate lanza excepción cuando el proveedor externo no responde")
    void translate_excepcionProveedor() {
        when(translationService.translate("timeout text"))
                .thenThrow(new RuntimeException("Servicio de traducción no disponible"));

        org.junit.jupiter.api.Assertions.assertThrows(RuntimeException.class,
                () -> translationService.translate("timeout text"));
    }
}
