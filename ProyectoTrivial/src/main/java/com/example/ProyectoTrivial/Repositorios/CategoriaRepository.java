package com.example.ProyectoTrivial.Repositorios;


import com.example.ProyectoTrivial.Juego.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    Optional<Categoria> findByNombre(String nombre);


}
