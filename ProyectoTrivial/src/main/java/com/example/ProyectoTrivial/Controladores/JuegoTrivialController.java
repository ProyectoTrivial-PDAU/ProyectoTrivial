package com.example.ProyectoTrivial.Controladores;

import com.example.ProyectoTrivial.Model.Pregunta;
import com.example.ProyectoTrivial.Servicios.JuegoTrivialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/trivial")
@CrossOrigin(origins = "*")
public class JuegoTrivialController {

    @Autowired
    private JuegoTrivialService juegoTrivialService;

    private com.example.ProyectoTrivial.Preguntas.Pregunta mapToDto(Pregunta p) {
        com.example.ProyectoTrivial.Preguntas.Pregunta dto = new com.example.ProyectoTrivial.Preguntas.Pregunta();
        dto.setCategoria(p.getCategoria() != null ? p.getCategoria().getNombre() : null);
        dto.setPregunta(p.getPregunta());
        List<String> opciones = p.getRespuestas() == null ? List.of() : p.getRespuestas().stream().map(r -> r.getTexto()).collect(Collectors.toList());
        dto.setOpciones(opciones);
        String correcta = null;
        if (p.getRespuestas() != null) {
            correcta = p.getRespuestas().stream().filter(r -> r.isEsCorrecta()).findFirst().map(r -> r.getTexto()).orElse(null);
        }
        dto.setRespuesta_correcta(correcta);
        return dto;
    }

    @GetMapping("/preguntas")
    public List<com.example.ProyectoTrivial.Preguntas.Pregunta> obtenerPreguntas(
            @RequestParam(required = false) String categoria,
            @RequestParam(defaultValue = "5") int cantidad) {

        List<Pregunta> modelos;
        if (categoria == null || categoria.isBlank()) {
            modelos = juegoTrivialService.obtenerPreguntasAleatorias(cantidad);
        } else {
            modelos = juegoTrivialService.obtenerPreguntas(categoria, cantidad);
        }

        return modelos.stream().map(this::mapToDto).collect(Collectors.toList());
    }
}

