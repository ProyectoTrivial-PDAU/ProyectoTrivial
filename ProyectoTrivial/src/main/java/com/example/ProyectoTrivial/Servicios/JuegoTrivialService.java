package com.example.ProyectoTrivial.Servicios;

import com.example.ProyectoTrivial.Model.Pregunta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * Orquesta una partida: selecciona preguntas, controla el flujo, calcula puntuación
 */
@Service
public class JuegoTrivialService {

    @Autowired
    private PreguntaService preguntaService;

    /**
     * Método para obtener preguntas por categoría
     * @param categoria
     * @param cantidad
     * @return
     */
    public List<Pregunta> obtenerPreguntas(String categoria, int cantidad) {
        return preguntaService.obtenerPorCategoria(categoria, cantidad);
    }

    /**
     * Método para obtener preguntas aleatorias
     * @param cantidad
     * @return
     */
    public List<Pregunta> obtenerPreguntasAleatorias(int cantidad) {
        return preguntaService.obtenerAleatorias(cantidad);
    }
}