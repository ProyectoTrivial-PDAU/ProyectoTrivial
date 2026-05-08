package com.example.ProyectoTrivial.Integracion;

import com.example.ProyectoTrivial.Model.Usuarios.Usuario;
import com.example.ProyectoTrivial.Repositorios.UsuarioRepository;
import com.example.ProyectoTrivial.Servicios.UsuarioService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.*;

/*
 * Pruebas de INTEGRACIÓN de UsuarioService con la base de datos H2.
 *
 * Criterio c): "Se han creado pruebas de integración"
 *    Se arranca el contexto real de Spring (H2 en memoria) y se comprueba
 *    que el servicio, el repositorio y el esquema de la BBDD funcionan juntos.
 */


@SpringBootTest
@ActiveProfiles("test")
@DisplayName("Pruebas de integración de UsuarioService y BBDD H2")
class UsuarioServiceIntegrationTest {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @AfterEach
    void limpiarBD() {
        usuarioRepository.deleteAll();
    }

    @Test
    @DisplayName("registrarUsuario persiste el usuario correctamente en BD")
    void registrarUsuario_persisteEnBD() {
        Usuario u = usuarioService.registrarUsuario("integ@test.com", "secreta123", "UserInteg");

        assertThat(u.getId()).isNotNull();
        assertThat(usuarioRepository.existsByEmail("integ@test.com")).isTrue();
    }

    @Test
    @DisplayName("loginUsuario devuelve el usuario tras registrarlo correctamente")
    void loginUsuario_traSRegistro_funciona() {
        usuarioService.registrarUsuario("login@test.com", "pass1234", "LoginUser");

        Usuario result = usuarioService.loginUsuario("login@test.com", "pass1234");

        assertThat(result.getEmail()).isEqualTo("login@test.com");
        assertThat(result.getNombre_usuario()).isEqualTo("LoginUser");
    }

    @Test
    @DisplayName("registrarUsuario lanza excepción con email duplicado en BD real")
    void registrarUsuario_emailDuplicado_excepcion() {
        usuarioService.registrarUsuario("dup@test.com", "pass", "User1");

        assertThatThrownBy(() ->
                usuarioService.registrarUsuario("dup@test.com", "otraPass", "User2"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("ya está registrado");
    }

    @Test
    @DisplayName("obtenerUsuarioPorId encuentra el usuario por su ID real de BD")
    void obtenerUsuarioPorId_encontrado() {
        Usuario guardado = usuarioService.registrarUsuario("find@test.com", "pass", "FindUser");

        Usuario encontrado = usuarioService.obtenerUsuarioPorId(guardado.getId());

        assertThat(encontrado.getEmail()).isEqualTo("find@test.com");
    }

    @Test
    @DisplayName("loginUsuario rechaza contraseña incorrecta con datos reales de BD")
    void loginUsuario_passwordIncorrecta_excepcion() {
        usuarioService.registrarUsuario("wrong@test.com", "correcta", "WrongUser");

        assertThatThrownBy(() ->
                usuarioService.loginUsuario("wrong@test.com", "malaPass"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Credenciales inválidas");
    }
}
