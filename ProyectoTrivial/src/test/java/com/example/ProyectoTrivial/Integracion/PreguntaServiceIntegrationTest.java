package com.example.ProyectoTrivial.Integracion;

import com.example.ProyectoTrivial.Model.Categoria;
import com.example.ProyectoTrivial.Model.Pregunta;
import com.example.ProyectoTrivial.Model.Respuesta;
import com.example.ProyectoTrivial.Repositorios.CategoriaRepository;
import com.example.ProyectoTrivial.Repositorios.PreguntaRepository;
import com.example.ProyectoTrivial.Servicios.PreguntaService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/*
 * Pruebas de INTEGRACIÓN de PreguntaService con la base de datos H2.
 *
 * Criterio c): "Se han creado pruebas de integración"
 * 
 *    Se verifica que el servicio interactúa correctamente con el repositorio
 *    y la BBDD real en un entorno de test controlado.
 */

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("Pruebas de integración de PreguntaService y BBDD H2")
class PreguntaServiceIntegrationTest {

    @Autowired
    private PreguntaService preguntaService;

    @Autowired
    private PreguntaRepository preguntaRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    private Categoria catHistoria;
    private Categoria catCiencia;

    @BeforeEach
    void setUp() {
        catHistoria = new Categoria();
        catHistoria.setNombre("Historia");
        catHistoria.setDescripcion("Preguntas de historia");
        catHistoria = categoriaRepository.save(catHistoria);

        catCiencia = new Categoria();
        catCiencia.setNombre("Ciencia");
        catCiencia.setDescripcion("Preguntas de ciencia");
        catCiencia = categoriaRepository.save(catCiencia);

        insertarPregunta(catHistoria, "¿En qué año cayó el Imperio Romano?");
        insertarPregunta(catHistoria, "¿Quién fue Julio César?");
        insertarPregunta(catCiencia, "¿Cuál es el símbolo del Oxígeno?");
    }

    @AfterEach
    void tearDown() {
        preguntaRepository.deleteAll();
        categoriaRepository.deleteAll();
    }

    @Test
    @DisplayName("obtenerPorCategoria devuelve solo preguntas de la categoría dada")
    void obtenerPorCategoria_soloCategoriaDada() {
        List<Pregunta> resultado = preguntaService.obtenerPorCategoria("Historia", 10);

        assertThat(resultado)
                .hasSize(2)
                .allMatch(p -> "Historia".equals(p.getCategoria().getNombre()));
    }

    @Test
    @DisplayName("obtenerPorCategoria respeta el límite de cantidad")
    void obtenerPorCategoria_respetaLimite() {
        List<Pregunta> resultado = preguntaService.obtenerPorCategoria("Historia", 1);

        assertThat(resultado).hasSize(1);
    }

    @Test
    @DisplayName("obtenerAleatorias devuelve preguntas de cualquier categoría")
    void obtenerAleatorias_devuelveMezcladas() {
        List<Pregunta> resultado = preguntaService.obtenerAleatorias(3);

        assertThat(resultado).hasSize(3);
    }

    @Test
    @DisplayName("cargarTodas devuelve todas las preguntas de la BD")
    void cargarTodas_devuelveTodas() {
        List<Pregunta> resultado = preguntaService.cargarTodas();

        assertThat(resultado).hasSize(3);
    }

    @Test
    @DisplayName("obtenerPorCategoria devuelve lista vacía si la categoría no tiene preguntas")
    void obtenerPorCategoria_categoriaVacia() {
        List<Pregunta> resultado = preguntaService.obtenerPorCategoria("Inexistente", 5);

        assertThat(resultado).isEmpty();
    }


    private void insertarPregunta(Categoria cat, String texto) {
        Pregunta p = new Pregunta();
        p.setCategoria(cat);
        p.setPregunta(texto);

        Respuesta r = new Respuesta();
        r.setTexto("Respuesta correcta");
        r.setEsCorrecta(true);
        r.setPregunta(p);

        p.setRespuestas(List.of(r));
        preguntaRepository.save(p);
    }
}
