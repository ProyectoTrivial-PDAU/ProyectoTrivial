package com.example.ProyectoTrivial.Servicios;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ProyectoTrivial.Model.Pregunta;
import com.example.ProyectoTrivial.Repositorios.PreguntaRepository;

@Service
public class PreguntaService {

    @Autowired
    private PreguntaRepository preguntaRepository;

    public List<Pregunta> obtenerAleatorias(int cantidad) {
        List<Pregunta> todas = preguntaRepository.findAll();
        Collections.shuffle(todas);
        return todas.stream().limit(cantidad).toList();
    }

    public List<Pregunta> obtenerPorCategoria(String categoria, int cantidad) {
        List<Pregunta> filtradas = preguntaRepository.findByCategoriaNombre(categoria);
        Collections.shuffle(filtradas);
        return filtradas.stream().limit(cantidad).toList();
    }

    public List<Pregunta> cargarTodas() {
        return preguntaRepository.findAll();
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
