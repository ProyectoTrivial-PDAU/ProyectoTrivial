package com.example.ProyectoTrivial.Controladores;

import com.example.ProyectoTrivial.Model.Pregunta;
import com.example.ProyectoTrivial.Servicios.JuegoTrivialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/*
Clase controladora para manejar las solicitudes relacionadas con el juego de trivial.
@RestController indica que esta clase es un controlador REST.
@RequestMapping("/api/trivial") define la ruta base para todas las solicitudes manejadas por este controlador.
@CrossOrigin(origins = "*") habilita CORS para este controlador, permitiendo solicitudes desde cualquier origen.
*/
@RestController
@RequestMapping("/api/trivial")
@CrossOrigin(origins = "*")
public class JuegoTrivialController {

    @Autowired
    private JuegoTrivialService juegoTrivialService;

    /*
    Metodo auxiliar para mapear un objeto Pregunta del modelo a un objeto Pregunta del DTO.
    */

    private com.example.ProyectoTrivial.DTO.PreguntaDTO mapToDto(Pregunta p) {
        com.example.ProyectoTrivial.DTO.PreguntaDTO dto = new com.example.ProyectoTrivial.DTO.PreguntaDTO();
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

    /**
     * Devuelve una lista de preguntas del trivial, opcionalmente filtradas por categoría y limitadas en cantidad.
     * @GetMapping("/preguntas") indica que este método maneja las solicitudes GET a la ruta /preguntas.
     * @RequestParam(required = false) String categoria permite filtrar las preguntas por categoría (opcional).
     * @RequestParam(defaultValue = "5") int cantidad especifica la cantidad máxima de preguntas a devolver (por defecto 5).
     * @return Lista de preguntas del trivial en formato DTO.
     */
    
    @GetMapping("/preguntas")
    public List<com.example.ProyectoTrivial.DTO.PreguntaDTO> obtenerPreguntas(
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

