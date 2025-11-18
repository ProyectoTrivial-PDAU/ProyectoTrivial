package com.example.ProyectoTrivial.Model.Partidas;

import com.example.ProyectoTrivial.Model.Usuarios.Usuario;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "PARTIDAS")
public class Partida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "USUARIO_ID")
    private Usuario usuario;


    private int puntuacion;


    @OneToMany(mappedBy = "partida", cascade = CascadeType.ALL)
    private List<PartidaPreguntas> preguntasRespondidas;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public int getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(int puntuacion) {
        this.puntuacion = puntuacion;
    }

    public List<PartidaPreguntas> getPreguntasRespondidas() {
        return preguntasRespondidas;
    }

    public void setPreguntasRespondidas(List<PartidaPreguntas> preguntasRespondidas) {
        this.preguntasRespondidas = preguntasRespondidas;
    }
}
