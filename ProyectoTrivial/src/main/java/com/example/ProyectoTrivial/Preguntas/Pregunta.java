package com.example.ProyectoTrivial.Preguntas;

import java.util.List;

public class Pregunta {
    private String categoria;
    private String pregunta;
    private List<String> opciones;
    private String respuesta_correcta;

    // Getters y setters
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public String getPregunta() { return pregunta; }
    public void setPregunta(String pregunta) { this.pregunta = pregunta; }

    public List<String> getOpciones() { return opciones; }
    public void setOpciones(List<String> opciones) { this.opciones = opciones; }

    public String getRespuesta_correcta() { return respuesta_correcta; }
    public void setRespuesta_correcta(String respuesta_correcta) { this.respuesta_correcta = respuesta_correcta; }
}
