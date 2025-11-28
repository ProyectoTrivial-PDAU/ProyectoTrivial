package com.example.ProyectoTrivial.Controladores;

import com.example.ProyectoTrivial.Model.Usuarios.Usuario;
import com.example.ProyectoTrivial.Servicios.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(originPatterns = "*", allowCredentials = "true")
public class AuthController {

    private final UsuarioService usuarioService;

    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

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