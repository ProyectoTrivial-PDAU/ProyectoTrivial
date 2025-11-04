package com.example.ProyectoTrivial.Partidas;

import com.example.ProyectoTrivial.Juego.Pregunta;
import com.example.ProyectoTrivial.Juego.Respuesta;
import jakarta.persistence.*;

@Entity
@Table(name = "PARTIDA_PREGUNTAS")
@IdClass(PartidaPreguntasID.class)
public class PartidaPreguntas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "PARTIDA_ID")
    private Partida partida;

    @ManyToOne
    @JoinColumn(name = "PREGUNTA_ID")
    private Pregunta pregunta;

    @ManyToOne
    @JoinColumn(name = "RESPUESTA_ID")
    private Respuesta respuesta;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
