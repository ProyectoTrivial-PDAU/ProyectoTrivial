package com.example.ProyectoTrivial.Controladores;

import com.example.ProyectoTrivial.DTO.PreguntaDTO;
import com.example.ProyectoTrivial.Model.Pregunta;
import com.example.ProyectoTrivial.Servicios.JuegoTrivialService;
import com.example.ProyectoTrivial.Servicios.OpenTDBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controlador principal del juego de trivial.
 * Fuente principal: API externa OpenTDB (miles de preguntas).
 * Fallback: Base de datos local si la API externa falla.
 * 
 * @param source (opcional) "local" para forzar preguntas de la BD, por defecto usa OpenTDB.
 */
@RestController
@RequestMapping("/api/trivial")
@CrossOrigin(origins = "*")
public class JuegoTrivialController {

    @Autowired
    private JuegoTrivialService juegoTrivialService;

    @Autowired
    private OpenTDBService openTDBService;

    /**
     * Mapea un objeto Pregunta del modelo (BD local) a PreguntaDTO.
     */
    private PreguntaDTO mapToDto(Pregunta p) {
        PreguntaDTO dto = new PreguntaDTO();
        dto.setCategoria(p.getCategoria() != null ? p.getCategoria().getNombre() : null);
        dto.setPregunta(p.getPregunta());
        List<String> opciones = p.getRespuestas() == null ? List.of() :
                p.getRespuestas().stream().map(r -> r.getTexto()).collect(Collectors.toList());
        dto.setOpciones(opciones);
        String correcta = null;
        if (p.getRespuestas() != null) {
            correcta = p.getRespuestas().stream()
                    .filter(r -> r.isEsCorrecta()).findFirst()
                    .map(r -> r.getTexto()).orElse(null);
        }
        dto.setRespuesta_correcta(correcta);
        return dto;
    }

    /**
     * Devuelve preguntas del trivial.
     * Por defecto usa la API externa OpenTDB para obtener preguntas variadas.
     * Si se pasa source=local, usa la base de datos.
     * Si OpenTDB falla, hace fallback automático a la BD.
     *
     * @param categoria Nombre de la categoría (en español).
     * @param cantidad  Número de preguntas (por defecto 5, máximo 50).
     * @param source    "local" para forzar BD, cualquier otro valor o vacío usa OpenTDB.
     */
    @GetMapping("/preguntas")
    public List<PreguntaDTO> obtenerPreguntas(
            @RequestParam(required = false) String categoria,
            @RequestParam(defaultValue = "5") int cantidad,
            @RequestParam(required = false) String source) {

        // Si se pide explícitamente fuente local, usar la BD
        if ("local".equalsIgnoreCase(source)) {
            return obtenerPreguntasLocales(categoria, cantidad);
        }

        // Intentar obtener de OpenTDB (fuente principal)
        try {
            List<PreguntaDTO> externas = openTDBService.getPreguntas(cantidad, categoria);
            if (externas != null && !externas.isEmpty()) {
                return externas;
            }
        } catch (Exception e) {
            System.err.println("OpenTDB falló, usando fallback a BD local: " + e.getMessage());
        }

        // Fallback: base de datos local
        return obtenerPreguntasLocales(categoria, cantidad);
    }

    /**
     * Obtiene preguntas de la base de datos local.
     */
    private List<PreguntaDTO> obtenerPreguntasLocales(String categoria, int cantidad) {
        List<Pregunta> modelos;
        if (categoria == null || categoria.isBlank()) {
            modelos = juegoTrivialService.obtenerPreguntasAleatorias(cantidad);
        } else {
            modelos = juegoTrivialService.obtenerPreguntas(categoria, cantidad);
        }
        return modelos.stream().map(this::mapToDto).collect(Collectors.toList());
    }
}

