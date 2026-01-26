package com.example.ProyectoTrivial.Model.Ranking;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/*
Clase que representa una entrada en el ranking global de jugadores.
@Entity indica que esta clase es una entidad JPA.
@Table especifica el nombre de la tabla en la base de datos.
@PrePersist se utiliza para establecer la fecha automáticamente antes de persistir la entidad. Persistir significa guardar o almacenar el estado de un objeto en una base de datos.
*/
@Entity
@Table(name = "ranking_global")
public class RankingGlobal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String jugador;

    private int puntuacion;

    @Column(name = "total_preguntas")
    private int totalPreguntas;

    private String categoria;

    private LocalDateTime fecha;

    @PrePersist
    public void prePersist() {
        this.fecha = LocalDateTime.now();
    }

    // Getters y setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getJugador() {
        return jugador;
    }

    public void setJugador(String jugador) {
        this.jugador = jugador;
    }

    public int getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(int puntuacion) {
        this.puntuacion = puntuacion;
    }

    public int getTotalPreguntas() {
        return totalPreguntas;
    }

    public void setTotalPreguntas(int totalPreguntas) {
        this.totalPreguntas = totalPreguntas;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

}
