package com.example.ProyectoTrivial.Servicios;

import com.example.ProyectoTrivial.Model.Usuarios.Usuario;
import com.example.ProyectoTrivial.Repositorios.UsuarioRepository;
import com.example.ProyectoTrivial.Servicios.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
 
import java.util.Optional;
 
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
 
/*
 * Pruebas UNITARIAS de UsuarioService.
 * Criterio b): "Se han creado pruebas unitarias"
 */

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas unitarias de UsuarioService")
class UsuarioServiceTest {
 
    @Mock
    private UsuarioRepository usuarioRepository;
 
    @Mock
    private PasswordEncoder passwordEncoder;
 
    @InjectMocks
    private UsuarioService usuarioService;
 
    private Usuario usuarioFijo;
 
    @BeforeEach
    void setUp() {
        usuarioFijo = new Usuario();
        usuarioFijo.setId(1L);
        usuarioFijo.setEmail("test@example.com");
        usuarioFijo.setPassword("$2a$10$hashedPassword");
        usuarioFijo.setNombre_usuario("TestUser");
    }
 
    // Prueba de registro de usuario
 
    @Test
    @DisplayName("registrarUsuario guarda el usuario con la contraseña hasheada")
    void registrarUsuario_guardaConPasswordHasheada() {
        when(usuarioRepository.existsByEmail("nuevo@example.com")).thenReturn(false);
        when(passwordEncoder.encode("miPassword")).thenReturn("$2a$hashed");
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));
 
        Usuario resultado = usuarioService.registrarUsuario("nuevo@example.com", "miPassword", "NuevoUser");
 
        assertThat(resultado.getEmail()).isEqualTo("nuevo@example.com");
        assertThat(resultado.getPassword()).isEqualTo("$2a$hashed");
        verify(passwordEncoder).encode("miPassword");
        verify(usuarioRepository).save(any(Usuario.class));
    }
 
    @Test
    @DisplayName("registrarUsuario lanza excepción si el email ya existe")
    void registrarUsuario_emailDuplicadoLanzaExcepcion() {
        when(usuarioRepository.existsByEmail("test@example.com")).thenReturn(true);
 
        assertThatThrownBy(() ->
                usuarioService.registrarUsuario("test@example.com", "pass", "User"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("ya está registrado");
 
        verify(usuarioRepository, never()).save(any());
    }
 
    @Test
    @DisplayName("registrarUsuario lanza excepción con email en blanco")
    void registrarUsuario_emailBlanco_lanzaExcepcion() {
        assertThatThrownBy(() ->
                usuarioService.registrarUsuario("", "password", "User"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Email inválido");
    }
 
    @Test
    @DisplayName("registrarUsuario lanza excepción con password en blanco")
    void registrarUsuario_passwordBlanca_lanzaExcepcion() {
        assertThatThrownBy(() ->
                usuarioService.registrarUsuario("email@test.com", "", "User"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Password inválida");
    }
 
    // Prueba de login de usuario
 
    @Test
    @DisplayName("loginUsuario devuelve el usuario con credenciales correctas")
    void loginUsuario_credencialesCorrectas() {
        when(usuarioRepository.findByEmail("test@example.com")).thenReturn(Optional.of(usuarioFijo));
        when(passwordEncoder.matches("miPassword", "$2a$10$hashedPassword")).thenReturn(true);
 
        Usuario resultado = usuarioService.loginUsuario("test@example.com", "miPassword");
 
        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getEmail()).isEqualTo("test@example.com");
    }
 
    @Test
    @DisplayName("loginUsuario lanza excepción si el email no existe")
    void loginUsuario_emailNoExiste_lanzaExcepcion() {
        when(usuarioRepository.findByEmail("noexiste@test.com")).thenReturn(Optional.empty());
 
        assertThatThrownBy(() ->
                usuarioService.loginUsuario("noexiste@test.com", "pass"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Usuario no encontrado");
    }
 
    @Test
    @DisplayName("loginUsuario lanza excepción con contraseña incorrecta")
    void loginUsuario_passwordIncorrecta_lanzaExcepcion() {
        when(usuarioRepository.findByEmail("test@example.com")).thenReturn(Optional.of(usuarioFijo));
        when(passwordEncoder.matches("malaPass", "$2a$10$hashedPassword")).thenReturn(false);
 
        assertThatThrownBy(() ->
                usuarioService.loginUsuario("test@example.com", "malaPass"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Credenciales inválidas");
    }
 
    // Prueba de obtención de usuario por ID
 
    @Test
    @DisplayName("obtenerUsuarioPorId devuelve el usuario cuando existe")
    void obtenerUsuarioPorId_existente() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioFijo));
 
        Usuario resultado = usuarioService.obtenerUsuarioPorId(1L);
 
        assertThat(resultado.getId()).isEqualTo(1L);
    }
 
    @Test
    @DisplayName("obtenerUsuarioPorId lanza excepción si el usuario no existe")
    void obtenerUsuarioPorId_inexistente_lanzaExcepcion() {
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());
 
        assertThatThrownBy(() -> usuarioService.obtenerUsuarioPorId(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Usuario no encontrado");
    }
}
