package com.example.ProyectoTrivial.E2E;

import com.example.ProyectoTrivial.Repositorios.PartidaRepository;
import com.example.ProyectoTrivial.Repositorios.UsuarioRepository;
import com.example.ProyectoTrivial.Servicios.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Pruebas E2E de los endpoints de autenticación y partidas.
 *
 * Criterio d): "Se han creado pruebas E2E"
 *  → Se simulan flujos completos de usuario: registro → login → guardar partida
 *    usando MockMvc con contexto de Spring completo y BD H2.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("Auth y Partidas - Pruebas E2E")
class AuthYPartidasE2ETest_FALLA {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PartidaRepository partidaRepository;

    @Autowired
    private UsuarioService usuarioService;

    @AfterEach
    void tearDown() {
        partidaRepository.deleteAll();
        usuarioRepository.deleteAll();
    }


    @Test
    @DisplayName("POST /api/auth/registro crea un usuario y devuelve sus datos")
    void registro_creaUsuario() throws Exception {
        Map<String, String> body = Map.of(
                "email", "e2e@test.com",
                "password", "segura123",
                "nombreUsuario", "E2EUser"
        );

        mockMvc.perform(post("/api/auth/registro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is("e2e@test.com")))
                .andExpect(jsonPath("$.nombre_usuario", is("E2EUser")))
                .andExpect(jsonPath("$.id", notNullValue()));
    }

    @Test
    @DisplayName("POST /api/auth/registro con email duplicado devuelve error")
    void registro_emailDuplicado_error() throws Exception {
        usuarioService.registrarUsuario("dup@test.com", "pass", "DupUser");

        Map<String, String> body = Map.of(
                "email", "dup@test.com",
                "password", "otraPass",
                "nombreUsuario", "OtroUser"
        );

        mockMvc.perform(post("/api/auth/registro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().is5xxServerError());
    }


    @Test
    @DisplayName("POST /api/auth/login con credenciales correctas devuelve el usuario")
    void login_credencialesCorrectas() throws Exception {
        usuarioService.registrarUsuario("login@test.com", "pass1234", "LoginUser");

        Map<String, String> body = Map.of(
                "email", "login@test.com",
                "password", "pass1234"
        );

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is("login@test.com")));
    }

    @Test
    @DisplayName("POST /api/auth/login con contraseña incorrecta devuelve error")
    void login_passwordIncorrecta_error() throws Exception {
        usuarioService.registrarUsuario("bad@test.com", "correcta", "BadUser");

        Map<String, String> body = Map.of(
                "email", "bad@test.com",
                "password", "incorrecta"
        );

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().is5xxServerError());
    }

    @Test
    @DisplayName("GET /partidas devuelve lista vacía cuando no hay partidas")
    void getPartidas_listaVacia() throws Exception {
        mockMvc.perform(get("/partidas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @DisplayName("POST /partidas/usuario/{id} guarda una partida asociada al usuario")
    void guardarPartida_OK() throws Exception {
        var usuario = usuarioService.registrarUsuario("partida@test.com", "pass", "PartidaUser");

        Map<String, Object> body = Map.of("puntuacion", 8);

        mockMvc.perform(post("/partidas/usuario/" + usuario.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.puntuacion", is(8)))
                .andExpect(jsonPath("$.id", notNullValue()));
    }
}
