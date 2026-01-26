package com.example.ProyectoTrivial.Repositorios;

import com.example.ProyectoTrivial.Model.Usuarios.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/*
Clase repositorio para la entidad Usuario.
Extiende JpaRepository para proporcionar operaciones CRUD y de consulta para la entidad Usuario.
Esto incluye métodos predefinidos para guardar, eliminar y buscar entidades Usuario en la base de datos.
*/
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);
    boolean existsByEmail(String email);
}
