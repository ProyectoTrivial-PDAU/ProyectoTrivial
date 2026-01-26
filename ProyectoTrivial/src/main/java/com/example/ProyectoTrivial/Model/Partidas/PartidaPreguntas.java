package com.example.ProyectoTrivial.Model.Partidas;

import com.example.ProyectoTrivial.Model.Pregunta;
import com.example.ProyectoTrivial.Model.Respuesta;
import jakarta.persistence.*;

/*
Clase que representa la relación entre una partida y las preguntas asociadas a ella,
incluyendo la respuesta seleccionada por el jugador.
@Entity indica que esta clase es una entidad JPA.
@Table especifica el nombre de la tabla en la base de datos.
@EmbeddedId indica que la clase utiliza una clave primaria compuesta.
@ManyToOne define las relaciones de muchos a uno con las entidades Partida, Pregunta y Respuesta.
@MapsId se utiliza para mapear las claves foráneas a los campos de la clave primaria compuesta.
@JoinColumn especifica las columnas de la tabla que se utilizan para las relaciones.
*/
@Entity
@Table(name = "PARTIDA_PREGUNTAS")
public class PartidaPreguntas {

    @EmbeddedId
    private PartidaPreguntasID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("partida")
    @JoinColumn(name = "PARTIDA_ID")
    private Partida partida;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("pregunta")
    @JoinColumn(name = "PREGUNTA_ID")
    private Pregunta pregunta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RESPUESTA_ID")
    private Respuesta respuesta;

    public PartidaPreguntasID getId() {
        return id;
    }

    public void setId(PartidaPreguntasID id) {
        this.id = id;
    }

    public Partida getPartida() {
        return partida;
    }

    public void setPartida(Partida partida) {
        this.partida = partida;
    }

    public Pregunta getPregunta() {
        return pregunta;
    }

    public void setPregunta(Pregunta pregunta) {
        this.pregunta = pregunta;
    }

    public Respuesta getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(Respuesta respuesta) {
        this.respuesta = respuesta;
    }
}
