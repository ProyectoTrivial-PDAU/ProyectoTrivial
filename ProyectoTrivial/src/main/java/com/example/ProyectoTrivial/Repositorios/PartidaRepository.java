package com.example.ProyectoTrivial.Repositorios;

import com.example.ProyectoTrivial.Model.Partidas.Partida;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * Repositorio de las partidas.
 * Los argumentos de JpaRepository son el tipo de dato que se maneja (Partida) y el tipo de dato que es el ID (Long)
 */
public interface PartidaRepository extends JpaRepository<Partida, Long> {



}
