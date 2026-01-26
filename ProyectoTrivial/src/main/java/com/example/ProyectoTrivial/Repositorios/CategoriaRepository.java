package com.example.ProyectoTrivial.Repositorios;


import com.example.ProyectoTrivial.Model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/*
Clase repositorio para la entidad Categoria.
Extiende JpaRepository para proporcionar operaciones CRUD y de consulta para la entidad Categoria.
Esto incluye métodos predefinidos para guardar, eliminar y buscar entidades Categoria en la base de datos.
*/
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    Optional<Categoria> findByNombre(String nombre);


}
