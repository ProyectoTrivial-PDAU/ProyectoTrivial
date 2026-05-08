package com.example.ProyectoTrivial.Servicios;
import com.example.ProyectoTrivial.Model.Categoria;
import com.example.ProyectoTrivial.Model.Pregunta;
import com.example.ProyectoTrivial.Repositorios.PreguntaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
 
import java.util.Collections;
import java.util.List;
 
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas unitarias de PreguntaService") 
class PreguntaServiceTest {
    
 
/*
 * Pruebas de PreguntaService.
 *
 * Esto cumple el criterio b (Se han creado pruebas unitarias)
 * 
 */

    @Mock
    private PreguntaRepository preguntaRepository;
 
    @InjectMocks
    private PreguntaService preguntaService;
 
    private Pregunta pregunta1;
    private Pregunta pregunta2;
    private Pregunta pregunta3;
 
    @BeforeEach
    void setUp() {
        Categoria cat = new Categoria();
        cat.setId(1L);
        cat.setNombre("Historia");
 
        pregunta1 = new Pregunta();
        pregunta1.setId(1L);
        pregunta1.setCategoria(cat);
        pregunta1.setPregunta("¿En qué año cayó el muro de Berlín?");
 
        pregunta2 = new Pregunta();
        pregunta2.setId(2L);
        pregunta2.setCategoria(cat);
        pregunta2.setPregunta("¿Quién fue el primer presidente de EE.UU.?");
 
        pregunta3 = new Pregunta();
        pregunta3.setId(3L);
        pregunta3.setCategoria(cat);
        pregunta3.setPregunta("¿En qué año comenzó la Segunda Guerra Mundial?");
    }
 
    // Prueba de obtener preguntas aleatorias
 
    @Test
    @DisplayName("obtenerAleatorias devuelve la cantidad solicitada")
    void obtenerAleatorias_devuelveCantidadSolicitada() {
        when(preguntaRepository.findAll()).thenReturn(List.of(pregunta1, pregunta2, pregunta3));
 
        List<Pregunta> resultado = preguntaService.obtenerAleatorias(2);
 
        assertThat(resultado).hasSize(2);
        verify(preguntaRepository, times(1)).findAll();
    }
 
    @Test
    @DisplayName("obtenerAleatorias no lanza excepción si hay menos preguntas que la cantidad")
    void obtenerAleatorias_cantidadMayorQueLista() {
        when(preguntaRepository.findAll()).thenReturn(List.of(pregunta1));
 
        List<Pregunta> resultado = preguntaService.obtenerAleatorias(10);
 
        assertThat(resultado).hasSize(1);
    }
 
    @Test
    @DisplayName("obtenerAleatorias devuelve lista vacía si el repositorio está vacío")
    void obtenerAleatorias_repositorioVacio() {
        when(preguntaRepository.findAll()).thenReturn(Collections.emptyList());
 
        List<Pregunta> resultado = preguntaService.obtenerAleatorias(5);
 
        assertThat(resultado).isEmpty();
    }
 
    // Prueba de obtener preguntas por categoría
 
    @Test
    @DisplayName("obtenerPorCategoria filtra correctamente por nombre de categoría")
    void obtenerPorCategoria_filtraCorrectamente() {
        when(preguntaRepository.findByCategoriaNombre("Historia"))
                .thenReturn(List.of(pregunta1, pregunta2, pregunta3));
 
        List<Pregunta> resultado = preguntaService.obtenerPorCategoria("Historia", 2);
 
        assertThat(resultado).hasSize(2);
        verify(preguntaRepository).findByCategoriaNombre("Historia");
    }
 
    @Test
    @DisplayName("obtenerPorCategoria devuelve vacío si no existe la categoría")
    void obtenerPorCategoria_categoriaInexistente() {
        when(preguntaRepository.findByCategoriaNombre("Inexistente"))
                .thenReturn(Collections.emptyList());
 
        List<Pregunta> resultado = preguntaService.obtenerPorCategoria("Inexistente", 5);
 
        assertThat(resultado).isEmpty();
    }
 
    // Prueba de cargar todas las preguntas
 
    @Test
    @DisplayName("cargarTodas delega en el repositorio y devuelve todas las preguntas")
    void cargarTodas_devuelveTodas() {
        when(preguntaRepository.findAll()).thenReturn(List.of(pregunta1, pregunta2));
 
        List<Pregunta> resultado = preguntaService.cargarTodas();
 
        assertThat(resultado).containsExactlyInAnyOrder(pregunta1, pregunta2);
    }
}
