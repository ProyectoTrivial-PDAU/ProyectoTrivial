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

    public List<Pregunta> obtenerPreguntas(String categoria, int cantidad) {
        return preguntaService.obtenerPorCategoria(categoria, cantidad);
    }

    public List<Pregunta> obtenerPreguntasAleatorias(int cantidad) {
        return preguntaService.obtenerAleatorias(cantidad);
    }
}
