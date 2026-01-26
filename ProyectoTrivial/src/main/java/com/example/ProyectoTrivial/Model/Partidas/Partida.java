package com.example.ProyectoTrivial.Model.Partidas;

import com.example.ProyectoTrivial.Model.Usuarios.Usuario;
import jakarta.persistence.*;

import java.util.List;

/*
Clase que representa una partida de trivial en el sistema.
@Entity indica que esta clase es una entidad JPA.
*/
@Entity
@Table(name = "PARTIDAS")
public class Partida {

    /*
     * Identificador único de la partida.
     * 
     * @Id indica que este campo es la clave primaria de la entidad.
     * 
     * @GeneratedValue(strategy = GenerationType.IDENTITY) indica que el valor de
     * este campo se genera automáticamente.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
     * @ManyToOne indica una relación de muchos a uno con la entidad Usuario.
     * 
     * @JoinColumn(name = "USUARIO_ID", nullable = false) especifica la columna de
     * la tabla que se utiliza para la relación.
     * 
     * fetch = FetchType.LAZY indica que la carga de la entidad relacionada se
     * realiza de forma perezosa. Esto significa que la entidad Usuario no se carga
     * automáticamente cuando se carga la entidad Partida, sino que se carga solo
     * cuando se accede a ella por primera vez.
     * 
     * orphanRemoval = true indica que si una Partida se elimina, las
     * PartidaPreguntas asociadas también se eliminan automáticamente.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USUARIO_ID", nullable = false)
    private Usuario usuario;

    @Column(name = "PUNTUACION", nullable = false)
    private int puntuacion;

    @OneToMany(mappedBy = "partida", cascade = CascadeType.ALL, orphanRemoval = true)
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
