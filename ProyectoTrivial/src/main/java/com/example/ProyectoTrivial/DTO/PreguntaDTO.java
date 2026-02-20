package com.example.ProyectoTrivial.DTO;

import java.util.List;

/*
Clase DTO (Data Transfer Object) para transferir datos de preguntas entre el servidor y el cliente.
Esta clase contiene los campos necesarios para representar una pregunta, incluyendo su categoría,
las opciones de respuesta y la respuesta correcta.
*/
public class PreguntaDTO {
    private String categoria;
    private String pregunta;
    private List<String> opciones;
    private String respuesta_correcta;

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getPregunta() {
        return pregunta;
    }

    public void setPregunta(String pregunta) {
        this.pregunta = pregunta;
    }

    public List<String> getOpciones() {
        return opciones;
    }

    public void setOpciones(List<String> opciones) {
        this.opciones = opciones;
    }

    public String getRespuesta_correcta() {
        return respuesta_correcta;
    }

    public void setRespuesta_correcta(String respuesta_correcta) {
        this.respuesta_correcta = respuesta_correcta;
    }
}
