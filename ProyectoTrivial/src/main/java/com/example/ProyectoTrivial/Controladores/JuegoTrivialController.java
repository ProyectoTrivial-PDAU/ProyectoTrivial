package com.example.ProyectoTrivial.Controladores;

import com.example.ProyectoTrivial.Model.Pregunta;
import com.example.ProyectoTrivial.Servicios.JuegoTrivialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trivial")
@CrossOrigin(origins = "*")
public class JuegoTrivialController {

    @Autowired
    private JuegoTrivialService juegoTrivialService;

    @GetMapping("/preguntas")
    public List<Pregunta> obtenerPreguntas(
            @RequestParam(required = false) String categoria,
            @RequestParam(defaultValue = "5") int cantidad) {

        if (categoria == null || categoria.isBlank()) {
            return juegoTrivialService.obtenerPreguntasAleatorias(cantidad);
        }

        return juegoTrivialService.obtenerPreguntas(categoria, cantidad);
    }
}

