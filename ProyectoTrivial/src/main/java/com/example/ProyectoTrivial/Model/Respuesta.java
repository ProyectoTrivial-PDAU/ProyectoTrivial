package com.example.ProyectoTrivial.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

/*
Clase que representa una respuesta en el sistema.
@Entity indica que esta clase es una entidad JPA.
@Table especifica el nombre de la tabla en la base de datos.
@Id indica el campo que es la clave primaria.
@GeneratedValue especifica la estrategia de generación de la clave primaria.
@ManyToOne define la relación de muchos a uno con la entidad Pregunta.
@JoinColumn especifica la columna que se utiliza para la unión con la tabla de preguntas.
@Column se utiliza para definir las propiedades de las columnas en la tabla.
@JsonBackReference se utiliza para manejar la serialización JSON y evitar referencias circulares. Esto es,
cuando se serializa una Pregunta, sus Respuestas se incluyen, pero cuando se serializa una Respuesta,
su Pregunta no se incluye de nuevo.
*/
@Entity
@Table(name = "RESPUESTAS")
public class Respuesta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "PREGUNTA_ID")
    @JsonBackReference
    private Pregunta pregunta;

    private String texto;

    @Column(name = "ES_CORRECTA")
    private boolean esCorrecta;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Pregunta getPregunta() {
        return pregunta;
    }

    public void setPregunta(Pregunta pregunta) {
        this.pregunta = pregunta;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public boolean isEsCorrecta() {
        return esCorrecta;
    }

    public void setEsCorrecta(boolean esCorrecta) {
        this.esCorrecta = esCorrecta;
    }
}
