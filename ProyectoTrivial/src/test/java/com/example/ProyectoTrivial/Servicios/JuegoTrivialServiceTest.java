package com.example.ProyectoTrivial.Servicios;
import com.example.ProyectoTrivial.Model.Categoria;
import com.example.ProyectoTrivial.Model.Pregunta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
 
import java.util.List;
 
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
 
/*
 * Pruebas UNITARIAS de JuegoTrivialService.
 * Criterio b): "Se han creado pruebas unitarias"
 */

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas unitarias de JuegoTrivialService")
class JuegoTrivialServiceTest {
 
    @Mock
    private PreguntaService preguntaService;
 
    @InjectMocks
    private JuegoTrivialService juegoTrivialService;
 
    private List<Pregunta> preguntasMock;
 
    @BeforeEach
    void setUp() {
        Categoria cat = new Categoria();
        cat.setNombre("Ciencia");
 
        Pregunta p1 = new Pregunta();
        p1.setId(1L);
        p1.setCategoria(cat);
        p1.setPregunta("¿Cuál es el símbolo del Oxígeno?");
 
        Pregunta p2 = new Pregunta();
        p2.setId(2L);
        p2.setCategoria(cat);
        p2.setPregunta("¿Cuántos planetas tiene el sistema solar?");
 
        preguntasMock = List.of(p1, p2);
    }
 
    //Prueba de obtención de preguntas por categoría
    @Test
    @DisplayName("obtenerPreguntas delega en preguntaService.obtenerPorCategoria")
    void obtenerPreguntas_delegaEnServicio() {
        when(preguntaService.obtenerPorCategoria("Ciencia", 2)).thenReturn(preguntasMock);
 
        List<Pregunta> resultado = juegoTrivialService.obtenerPreguntas("Ciencia", 2);
 
        assertThat(resultado).hasSize(2);
        verify(preguntaService, times(1)).obtenerPorCategoria("Ciencia", 2);
        verifyNoMoreInteractions(preguntaService);
    }
 
    //Prueba de obtención de preguntas aleatorias
    @Test
    @DisplayName("obtenerPreguntasAleatorias delega en preguntaService.obtenerAleatorias")
    void obtenerPreguntasAleatorias_delegaEnServicio() {
        when(preguntaService.obtenerAleatorias(2)).thenReturn(preguntasMock);
 
        List<Pregunta> resultado = juegoTrivialService.obtenerPreguntasAleatorias(2);
 
        assertThat(resultado).hasSize(2);
        verify(preguntaService, times(1)).obtenerAleatorias(2);
        verifyNoMoreInteractions(preguntaService);
    }
 
    //Prueba de obtención de preguntas por categoría sin resultados
    @Test
    @DisplayName("obtenerPreguntas devuelve lista vacía si la categoría no tiene preguntas")
    void obtenerPreguntas_sinResultados() {
        when(preguntaService.obtenerPorCategoria("Inexistente", 5)).thenReturn(List.of());
 
        List<Pregunta> resultado = juegoTrivialService.obtenerPreguntas("Inexistente", 5);
 
        assertThat(resultado).isEmpty();
    }
}
 