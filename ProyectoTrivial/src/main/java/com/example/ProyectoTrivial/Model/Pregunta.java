package com.example.ProyectoTrivial.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.List;

/*
Clase que representa una pregunta en el sistema.
@Entity indica que esta clase es una entidad JPA.
@Table especifica el nombre de la tabla en la base de datos.
@Id indica el campo que es la clave primaria.
@GeneratedValue especifica la estrategia de generación de la clave primaria.
@ManyToOne define la relación de muchos a uno con la entidad Categoria.
@JoinColumn especifica la columna que se utiliza para la unión con la tabla de categorías.
@Column se utiliza para definir las propiedades de las columnas en la tabla.
@OneToMany define la relación de uno a muchos con la entidad Respuesta.
cascade = CascadeType.ALL indica que las operaciones en Pregunta se propagan a Respuesta. Esto es, si se elimina una Pregunta, 
también se eliminan sus Respuestas asociadas.
*/
@Entity
@Table(name = "PREGUNTAS")
public class Pregunta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "CATEGORIA_ID")
    @JsonBackReference
    private Categoria categoria;

    @Column(name = "TEXTO")
    private String pregunta;

    @OneToMany(mappedBy = "pregunta", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Respuesta> respuestas;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public String getPregunta() {
        return pregunta;
    }

    public void setPregunta(String pregunta) {
        this.pregunta = pregunta;
    }

    public List<Respuesta> getRespuestas() {
        return respuestas;
    }

    public void setRespuestas(List<Respuesta> respuestas) {
        this.respuestas = respuestas;
    }
}