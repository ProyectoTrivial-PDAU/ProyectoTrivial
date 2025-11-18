package com.example.ProyectoTrivial.Model.Partidas;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class PartidaPreguntasID implements Serializable {

    private Long partida;
    private Long pregunta;

    public PartidaPreguntasID(){}

    public PartidaPreguntasID(Long partida, Long pregunta) {
        this.partida = partida;
        this.pregunta = pregunta;
    }

    public Long getPartida() {
        return partida;
    }

    public void setPartida(Long partida) {
        this.partida = partida;
    }

    public Long getPregunta() {
        return pregunta;
    }

    public void setPregunta(Long pregunta) {
        this.pregunta = pregunta;
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (!(o instanceof PartidaPreguntasID)) return false;
        PartidaPreguntasID that = (PartidaPreguntasID) o;
        return Objects.equals(partida, that.partida) && Objects.equals(pregunta, that.pregunta);
    }
    @Override
    public int hashCode(){
        return Objects.hash(partida, pregunta);
    }

}
