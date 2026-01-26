package com.example.ProyectoTrivial.Servicios;

import com.example.ProyectoTrivial.Model.Usuarios.Usuario;
import com.example.ProyectoTrivial.Repositorios.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/*
Clase de servicio para gestionar la lógica relacionada con los usuarios.
Utiliza UsuarioRepository para realizar operaciones CRUD en las entidades Usuario.
@Service indica que esta clase es un servicio de Spring, lo que permite la inyección de dependencias y la gestión del ciclo de vida del bean.
@Autowired se utiliza para inyectar automáticamente la dependencia de UsuarioRepository en esta clase.
Optional se utiliza para manejar la posible ausencia de un valor (por ejemplo, cuando un usuario no se encuentra en la base de datos).
*/
@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario registrarUsuario(String email, String password, String nombreUsuario) {
        if (email == null || email.isBlank()) throw new IllegalArgumentException("Email inválido");
        if (password == null || password.isBlank()) throw new IllegalArgumentException("Password inválida");
        if (usuarioRepository.existsByEmail(email)) {
            throw new RuntimeException("El email ya está registrado");
        }
        Usuario u = new Usuario();
        u.setEmail(email);
        u.setPassword(password);
        u.setNombre_usuario(nombreUsuario);
        return usuarioRepository.save(u);
    }

    public Usuario loginUsuario(String email, String password) {
        Optional<Usuario> opt = usuarioRepository.findByEmail(email);
        if (opt.isEmpty()) throw new RuntimeException("Usuario no encontrado");
        Usuario u = opt.get();
        if (!u.getPassword().equals(password)) throw new RuntimeException("Credenciales inválidas");
        return u;
    }

    public Usuario obtenerUsuarioPorId(Long id) {
        return usuarioRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }
}
