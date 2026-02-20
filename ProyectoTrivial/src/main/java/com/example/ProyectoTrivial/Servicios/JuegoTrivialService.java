package com.example.ProyectoTrivial.Servicios;

import com.example.ProyectoTrivial.Model.Pregunta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/*
Clase de servicio para gestionar la lógica del juego Trivial.
Utiliza PreguntaService para obtener preguntas según la categoría o de forma aleatoria.
@Service indica que esta clase es un servicio de Spring, lo que permite la inyección de dependencias y la gestión del ciclo de vida del bean.
@Autowired se utiliza para inyectar automáticamente la dependencia de PreguntaService en esta clase.
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
