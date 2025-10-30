package com.example.ProyectoTrivial.Servicios;

import com.example.ProyectoTrivial.Preguntas.ContenedorJSON;
import com.example.ProyectoTrivial.Preguntas.Pregunta;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.core.io.ClassPathResource;


/**
 * Gestiona el acceso y lógica de las preguntas individuales (CRUD, filtrado, etc.)
 */
@Service
public class PreguntaService {

    public List<Pregunta> cargarTodas() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            InputStream is = new ClassPathResource("Preguntas/Preguntas.json").getInputStream();
            ContenedorJSON contenedor = mapper.readValue(is, ContenedorJSON.class);
            return contenedor.getTrivial();
        } catch (Exception e) {
            e.printStackTrace(); // muestra el error en consola
            throw new RuntimeException("Error al cargar preguntas", e);
        }
    }



    // Método para preguntas por categoría
    public List<Pregunta> obtenerPorCategoria(String categoria, int cantidad) {
        List<Pregunta> todas = cargarTodas();
        List<Pregunta> filtradas = todas.stream()
                .filter(p -> p.getCategoria().equalsIgnoreCase(categoria))
                .collect(Collectors.toList());
        Collections.shuffle(filtradas);
        return filtradas.stream().limit(cantidad).collect(Collectors.toList());
    }

    // Método para preguntas aleatorias
    public List<Pregunta> obtenerAleatorias(int cantidad) {
        List<Pregunta> todas = new ArrayList<>(cargarTodas());
        Collections.shuffle(todas);
        return todas.stream().limit(cantidad).collect(Collectors.toList());
    }
}

