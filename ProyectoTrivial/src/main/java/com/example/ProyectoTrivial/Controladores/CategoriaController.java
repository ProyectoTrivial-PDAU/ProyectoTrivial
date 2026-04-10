package com.example.ProyectoTrivial.Controladores;

import com.example.ProyectoTrivial.Model.Pregunta;
import com.example.ProyectoTrivial.Servicios.OpenTDBService;
import com.example.ProyectoTrivial.Servicios.PreguntaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Controlador de categorías.
 * Combina las categorías de OpenTDB (API externa) con las de la BD local.
 */
@RestController
@RequestMapping("/api/trivial")
@CrossOrigin(origins = "*")
public class CategoriaController {

    private final PreguntaService preguntaService;
    private final OpenTDBService openTDBService;

    @Autowired
    public CategoriaController(PreguntaService preguntaService, OpenTDBService openTDBService) {
        this.preguntaService = preguntaService;
        this.openTDBService = openTDBService;
    }

    /**
     * Devuelve todas las categorías disponibles.
     * Combina las categorías de OpenTDB con las de la BD local (sin duplicados).
     */
    @GetMapping("/categorias")
    public List<String> obtenerCategorias() {
        Set<String> categorias = new TreeSet<>(); // TreeSet para orden alfabético automático

        // Categorías de OpenTDB (API externa)
        try {
            List<String> externas = openTDBService.getCategorias();
            if (externas != null) {
                categorias.addAll(externas);
            }
        } catch (Exception e) {
            System.err.println("Error obteniendo categorías de OpenTDB: " + e.getMessage());
        }

        // Categorías de la BD local
        try {
            List<Pregunta> todas = preguntaService.cargarTodas();
            todas.stream()
                    .map(p -> p.getCategoria() == null ? null : p.getCategoria().getNombre())
                    .filter(Objects::nonNull)
                    .forEach(categorias::add);
        } catch (Exception e) {
            System.err.println("Error obteniendo categorías locales: " + e.getMessage());
        }

        return new ArrayList<>(categorias);
    }
}
