package com.example.ProyectoTrivial.Repositorios;

import com.example.ProyectoTrivial.Juego.Pregunta;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PreguntaRepository extends JpaRepository<Pregunta, Long> {
    List<Pregunta> findByCategoriaNombre(String nombre);

}
