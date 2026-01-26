package com.example.ProyectoTrivial.Controladores;

import com.example.ProyectoTrivial.Model.Usuarios.Usuario;
import com.example.ProyectoTrivial.Servicios.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/*
Clase controladora para manejar las solicitudes de autenticación (registro e inicio de sesión) de usuarios.
@RestController indica que esta clase es un controlador REST.
@RequestMapping("/api/auth") define la ruta base para todas las solicitudes manejadas por este controlador.
@CrossOrigin configura las políticas de CORS para este controlador, permitiendo solicitudes desde cualquier origen y permitiendo el envío de credenciales.  
*/
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(originPatterns = "*", allowCredentials = "true")
public class AuthController {

    private final UsuarioService usuarioService;

    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    /*
    Método para manejar las solicitudes de registro de usuarios.
    @PostMapping("/register") indica que este método maneja las solicitudes POST a la ruta /register.
    @RequestBody Map<String, String> body indica que el cuerpo de la solicitud se mapea a un objeto Map.
    ResponseEntity<?> es una respuesta HTTP genérica que puede contener cualquier tipo de cuerpo.
    */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> body) {
        try {
            Usuario u = usuarioService.registrarUsuario(
                    body.get("email"),
                    body.get("password"),
                    body.get("nombre_usuario")
            );
            return ResponseEntity.ok(u);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /*
    Método para manejar las solicitudes de inicio de sesión de usuarios.
    @PostMapping("/login") indica que este método maneja las solicitudes POST a la ruta /login.
    @RequestBody Map<String, String> body indica que el cuerpo de la solicitud se mapea a un objeto Map.
    ResponseEntity<?> es una respuesta HTTP genérica que puede contener cualquier tipo de cuerpo.
    */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        try {
            Usuario u = usuarioService.loginUsuario(
                    body.get("email"),
                    body.get("password")
            );
            return ResponseEntity.ok(u);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}