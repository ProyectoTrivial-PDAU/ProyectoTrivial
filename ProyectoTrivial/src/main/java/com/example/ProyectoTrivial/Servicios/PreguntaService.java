package com.example.ProyectoTrivial.Servicios;

import com.example.ProyectoTrivial.Model.Pregunta;
import com.example.ProyectoTrivial.Repositorios.PreguntaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Gestiona el acceso y lógica de las preguntas individuales (CRUD, filtrado, etc.)
 */
@Service
public class PreguntaService {

    @Autowired
    private PreguntaRepository preguntaRepository;

    public List<String> obtenerCategorias(){
        return preguntaRepository.findAll().stream()
                .map(p -> p.getCategoria().getNombre())
                .distinct()
                .collect(Collectors.toList());
    }

    // Método para preguntas por categoría
    public List<Pregunta> obtenerPorCategoria(String categoria, int cantidad) {
        List<Pregunta> preguntasFiltradas = preguntaRepository.findByCategoriaNombre(categoria);
        Collections.shuffle(preguntasFiltradas);
        return preguntasFiltradas.stream().limit(cantidad).collect(Collectors.toList());
    }

    // Método para preguntas aleatorias
    public List<Pregunta> obtenerAleatorias(int cantidad) {
        List<Pregunta> todas = preguntaRepository.findAll();
        Collections.shuffle(todas);
        return todas.stream().limit(cantidad).collect(Collectors.toList());
    }
}

