package com.example.ProyectoTrivial.Controladores;

import com.example.ProyectoTrivial.Servicios.PreguntaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
     * Devuelve una lista con todas las categorías disponibles
     * a partir del JSON de preguntas.
     */
    @GetMapping("/categorias")
    public List<String> obtenerCategorias() {
        return preguntaService.obtenerCategorias();

    }
}
