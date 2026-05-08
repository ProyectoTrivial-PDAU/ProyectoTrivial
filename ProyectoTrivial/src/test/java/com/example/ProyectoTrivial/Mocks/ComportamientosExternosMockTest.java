package com.example.ProyectoTrivial.Mocks;

import com.example.ProyectoTrivial.DTO.PreguntaDTO;
import com.example.ProyectoTrivial.Servicios.JuegoTrivialService;
import com.example.ProyectoTrivial.Servicios.OpenTDBService;
import com.example.ProyectoTrivial.Servicios.PreguntaService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Pruebas con MOCKS que simulan comportamientos externos.
 *
 * Criterio e): "Se han simulado comportamientos externos"
 *    Se usan Mocks de Mockito para imitar respuestas de servicios externos
 *    (OpenTDB API, PreguntaService) sin hacer llamadas reales.
 *    Se simulan escenarios difíciles de reproducir en real:
 *    timeout, respuesta vacía, excepción de red, etc.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Simulación de comportamientos externos con Mockito")
class ComportamientosExternosMockTest {

    @Mock
    private OpenTDBService openTDBService;

    @Mock
    private PreguntaService preguntaService;

    @InjectMocks
    private JuegoTrivialService juegoTrivialService;


    @Test
    @DisplayName("OpenTDB responde correctamente: se devuelven las preguntas recibidas")
    void openTDB_respuestaCorrecta_devuelvePreguntas() {
        PreguntaDTO dto1 = crearDTO("¿Capital de Alemania?", "Berlín");
        PreguntaDTO dto2 = crearDTO("¿Capital de Italia?", "Roma");

        when(openTDBService.getPreguntas(2, "Geografía")).thenReturn(List.of(dto1, dto2));

        List<PreguntaDTO> resultado = openTDBService.getPreguntas(2, "Geografía");

        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).getRespuesta_correcta()).isEqualTo("Berlín");
        verify(openTDBService, times(1)).getPreguntas(2, "Geografía");
    }


    @Test
    @DisplayName("OpenTDB devuelve lista vacía: el sistema debe manejarlo sin lanzar excepción")
    void openTDB_respuestaVacia_sinExcepcion() {
        when(openTDBService.getPreguntas(anyInt(), anyString())).thenReturn(List.of());

        List<PreguntaDTO> resultado = openTDBService.getPreguntas(5, "Historia");

        assertThat(resultado).isEmpty();
    }

    @Test
    @DisplayName("OpenTDB lanza RuntimeException: el mock lo simula correctamente")
    void openTDB_excepcionRed_simulada() {
        when(openTDBService.getPreguntas(anyInt(), anyString()))
                .thenThrow(new RuntimeException("Timeout de conexión a OpenTDB"));

        org.junit.jupiter.api.Assertions.assertThrows(RuntimeException.class, () ->
                openTDBService.getPreguntas(5, "Ciencia"));
    }


    @Test
    @DisplayName("OpenTDB falla en primer intento y devuelve datos en segundo (retry simulado)")
    void openTDB_primerFalloDespuesExito() {
        PreguntaDTO dto = crearDTO("¿Qué planeta es el más grande?", "Júpiter");

        when(openTDBService.getPreguntas(1, "Ciencia"))
                .thenThrow(new RuntimeException("Error temporal"))
                .thenReturn(List.of(dto));

        // Primer intento: falla
        org.junit.jupiter.api.Assertions.assertThrows(RuntimeException.class,
                () -> openTDBService.getPreguntas(1, "Ciencia"));

        // Segundo intento: éxito
        List<PreguntaDTO> resultado = openTDBService.getPreguntas(1, "Ciencia");
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getPregunta()).isEqualTo("¿Qué planeta es el más grande?");
    }


    @Test
    @DisplayName("PreguntaService simulado devuelve preguntas de una categoría específica")
    void preguntaService_categoriaEspecifica_simulada() {
        var preguntaMock = new com.example.ProyectoTrivial.Model.Pregunta();
        preguntaMock.setId(42L);
        preguntaMock.setPregunta("¿Cuánto es 2+2?");

        when(preguntaService.obtenerPorCategoria("Matemáticas", 1))
                .thenReturn(List.of(preguntaMock));

        var resultado = juegoTrivialService.obtenerPreguntas("Matemáticas", 1);

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getPregunta()).isEqualTo("¿Cuánto es 2+2?");
    }


    @Test
    @DisplayName("openTDBService.getPreguntas es llamado exactamente una vez por petición")
    void openTDB_verificaNumeroLlamadas() {
        when(openTDBService.getPreguntas(anyInt(), anyString())).thenReturn(List.of());

        openTDBService.getPreguntas(5, "Arte");

        verify(openTDBService, times(1)).getPreguntas(5, "Arte");
        verify(openTDBService, never()).getPreguntas(5, "Historia");
    }


    private PreguntaDTO crearDTO(String pregunta, String correcta) {
        PreguntaDTO dto = new PreguntaDTO();
        dto.setPregunta(pregunta);
        dto.setRespuesta_correcta(correcta);
        dto.setOpciones(List.of(correcta, "Opción B", "Opción C", "Opción D"));
        dto.setCategoria("Test");
        return dto;
    }
}
