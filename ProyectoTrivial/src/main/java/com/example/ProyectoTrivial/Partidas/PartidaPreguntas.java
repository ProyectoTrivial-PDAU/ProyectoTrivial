package com.example.ProyectoTrivial.Partidas;

import com.example.ProyectoTrivial.Juego.Pregunta;
import com.example.ProyectoTrivial.Juego.Respuesta;
import jakarta.persistence.*;

@Entity
@Table(name = "PARTIDA_PREGUNTAS")
public class PartidaPreguntas {

    @EmbeddedId
    private PartidaPreguntasID id;

    @ManyToOne
    @MapsId("partida")
    @JoinColumn(name = "PARTIDA_ID")
    private Partida partida;

    @ManyToOne
    @MapsId("partida")
    @JoinColumn(name = "PREGUNTA_ID")
    private Pregunta pregunta;

    @ManyToOne
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
