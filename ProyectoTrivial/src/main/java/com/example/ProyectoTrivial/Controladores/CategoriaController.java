package com.example.ProyectoTrivial.Controladores;

import com.example.ProyectoTrivial.Model.Pregunta;
import com.example.ProyectoTrivial.Servicios.PreguntaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/*
Clase controladora para manejar las solicitudes relacionadas con las categorías de preguntas del trivial.
@RestController indica que esta clase es un controlador REST.
@RequestMapping("/api/trivial") define la ruta base para todas las solicitudes manejadas por este controlador.
@CrossOrigin(origins = "*") habilita CORS para este controlador, permitiendo solicitudes desde cualquier origen.
*/
@RestController
@RequestMapping("/api/trivial")
@CrossOrigin(origins = "*") // cambia el puerto si tu front corre en otro //(origins = "http://127.0.0.1:5500") para live server de vscode
public class CategoriaController {

    private final PreguntaService preguntaService;

    @Autowired
    public CategoriaController(PreguntaService preguntaService) {
        this.preguntaService = preguntaService;
    }

    /**
     * Devuelve una lista con todas las categorías disponibles.
     * @GetMapping("/categorias") indica que este método maneja las solicitudes GET a la ruta /categorias.
     * @return Lista de nombres de categorías.
     */
    @GetMapping("/categorias")
    public List<String> obtenerCategorias() {
        List<Pregunta> todas = preguntaService.cargarTodas();

        return todas.stream()
                .map(p -> p.getCategoria() == null ? null : p.getCategoria().getNombre())
                .filter(Objects::nonNull)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }
}
