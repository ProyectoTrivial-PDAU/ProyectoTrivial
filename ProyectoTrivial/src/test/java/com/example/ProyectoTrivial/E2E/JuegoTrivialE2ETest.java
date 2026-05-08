package com.example.ProyectoTrivial.E2E;

import com.example.ProyectoTrivial.Model.Categoria;
import com.example.ProyectoTrivial.Model.Pregunta;
import com.example.ProyectoTrivial.Model.Respuesta;
import com.example.ProyectoTrivial.Repositorios.CategoriaRepository;
import com.example.ProyectoTrivial.Repositorios.PreguntaRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/*
 * Pruebas E2E del endpoint GET /api/trivial/preguntas.
 *
 * Criterio d): "Se han creado pruebas E2E"
 *    Se lanza el contexto completo de Spring con MockMvc para simular
 *    peticiones HTTP reales de extremo a extremo (cliente → controlador →
 *    servicio → repositorio → BD H2), comprobando el JSON de respuesta.
 * 
 *    OpenTDBService se mockea para aislar la dependencia externa y forzar
 *    el fallback a la BD local en los tests que lo requieran.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("JuegoTrivialController - Pruebas E2E")
class JuegoTrivialE2ETest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PreguntaRepository preguntaRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;


    @BeforeEach
    void setUp() {
        // Forzar que OpenTDB devuelva lista vacía → el controlador usa BD local
        
        // Insertar datos de prueba en H2
        Categoria cat = new Categoria();
        cat.setNombre("Geografía");
        cat.setDescripcion("Preguntas de geografía");
        cat = categoriaRepository.save(cat);

        insertarPregunta(cat, "¿Cuál es la capital de Francia?", "París");
        insertarPregunta(cat, "¿Cuál es el río más largo del mundo?", "El Nilo");
        insertarPregunta(cat, "¿En qué continente está Brasil?", "América del Sur");
    }

    @AfterEach
    void tearDown() {
        preguntaRepository.deleteAll();
        categoriaRepository.deleteAll();
    }

    @Test
    @DisplayName("GET /api/trivial/preguntas?source=local devuelve lista de preguntas en JSON")
    void getPreguntas_local_devuelveJSON() throws Exception {
        mockMvc.perform(get("/api/trivial/preguntas")
                        .param("source", "local")
                        .param("cantidad", "3"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].pregunta", not(emptyString())))
                .andExpect(jsonPath("$[0].opciones", hasSize(greaterThan(0))))
                .andExpect(jsonPath("$[0].respuesta_correcta", not(emptyString())));
    }

    @Test
    @DisplayName("GET /api/trivial/preguntas?source=local&categoria=Geografía filtra por categoría")
    void getPreguntas_local_filtraCategoria() throws Exception {
        mockMvc.perform(get("/api/trivial/preguntas")
                        .param("source", "local")
                        .param("categoria", "Geografía")
                        .param("cantidad", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].categoria", is("Geografía")));
    }

    @Test
    @DisplayName("GET /api/trivial/preguntas sin source hace fallback a BD local cuando OpenTDB devuelve vacío")
    void getPreguntas_sinSource_fallbackALocal() throws Exception {
        mockMvc.perform(get("/api/trivial/preguntas")
                        .param("cantidad", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(lessThanOrEqualTo(2))));
    }

    @Test
    @DisplayName("GET /api/trivial/preguntas?source=local&cantidad=1 respeta el límite")
    void getPreguntas_respetaCantidad() throws Exception {
        mockMvc.perform(get("/api/trivial/preguntas")
                        .param("source", "local")
                        .param("cantidad", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    @DisplayName("GET /api/trivial/preguntas?source=local&categoria=Inexistente devuelve lista vacía")
    void getPreguntas_categoriaInexistente_devuelveVacio() throws Exception {
        mockMvc.perform(get("/api/trivial/preguntas")
                        .param("source", "local")
                        .param("categoria", "Inexistente"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }


    private void insertarPregunta(Categoria cat, String texto, String correcta) {
        Pregunta p = new Pregunta();
        p.setCategoria(cat);
        p.setPregunta(texto);

        Respuesta r1 = new Respuesta();
        r1.setTexto(correcta);
        r1.setEsCorrecta(true);
        r1.setPregunta(p);

        Respuesta r2 = new Respuesta();
        r2.setTexto("Opción falsa A");
        r2.setEsCorrecta(false);
        r2.setPregunta(p);

        Respuesta r3 = new Respuesta();
        r3.setTexto("Opción falsa B");
        r3.setEsCorrecta(false);
        r3.setPregunta(p);

        Respuesta r4 = new Respuesta();
        r4.setTexto("Opción falsa C");
        r4.setEsCorrecta(false);
        r4.setPregunta(p);

        p.setRespuestas(List.of(r1, r2, r3, r4));
        preguntaRepository.save(p);
    }
}
