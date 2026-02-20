package com.example.ProyectoTrivial.Repositorios;

import com.example.ProyectoTrivial.Model.Pregunta;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/*
Clase repositorio para la entidad Pregunta.
Extiende JpaRepository para proporcionar operaciones CRUD y de consulta para la entidad Pregunta.
Esto incluye métodos predefinidos para guardar, eliminar y buscar entidades Pregunta en la base de datos.
*/
public interface PreguntaRepository extends JpaRepository<Pregunta, Long> {
    List<Pregunta> findByCategoriaNombre(String nombre);

}
