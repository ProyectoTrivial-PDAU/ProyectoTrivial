package com.example.ProyectoTrivial.Servicios;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ProyectoTrivial.Model.Pregunta;
import com.example.ProyectoTrivial.Repositorios.PreguntaRepository;

/*
Clase de servicio para gestionar la lógica relacionada con las preguntas.
Utiliza PreguntaRepository para realizar operaciones CRUD en las entidades Pregunta.
@Service indica que esta clase es un servicio de Spring, lo que permite la inyección de dependencias y la gestión del ciclo de vida del bean.
@Autowired se utiliza para inyectar automáticamente la dependencia de PreguntaRepository en esta clase.
.stream() habilita el procesamiento funcional de las colecciones. 
*/
@Service
public class PreguntaService {

    @Autowired
    private PreguntaRepository preguntaRepository;

    public List<Pregunta> obtenerAleatorias(int cantidad) {
        List<Pregunta> todas = new ArrayList<>(preguntaRepository.findAll());
        Collections.shuffle(todas);
        return todas.stream().limit(cantidad).toList();
    }

    public List<Pregunta> obtenerPorCategoria(String categoria, int cantidad) {
        List<Pregunta> filtradas = new ArrayList<>(preguntaRepository.findByCategoriaNombre(categoria));
        Collections.shuffle(filtradas);
        return filtradas.stream().limit(cantidad).toList();
    }

    public List<Pregunta> cargarTodas() {
        return new ArrayList<>(preguntaRepository.findAll());
    }
}


























/*
package com.example.ProyectoTrivial.Servicios;

import com.example.ProyectoTrivial.Model.Pregunta;
import com.example.ProyectoTrivial.Repositorios.PreguntaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PreguntaService {

    @Autowired
    private PreguntaRepository preguntaRepository;

    public List<Pregunta> cargarTodas() {
        return preguntaRepository.findAll();
    }

    public List<Pregunta> obtenerPorCategoria(String categoria, int cantidad) {
        List<Pregunta> filtradas = preguntaRepository.findByCategoriaNombre(categoria);
        Collections.shuffle(filtradas);
        return filtradas.stream().limit(cantidad).collect(Collectors.toList());
    }

    public List<Pregunta> obtenerAleatorias(int cantidad) {
        List<Pregunta> todas = preguntaRepository.findAll();
        Collections.shuffle(todas);
        return todas.stream().limit(cantidad).collect(Collectors.toList());
    }
}
*/
