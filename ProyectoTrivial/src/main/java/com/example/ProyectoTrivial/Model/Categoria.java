package com.example.ProyectoTrivial.Model;

import jakarta.persistence.*;

/*
clase que representa una categoría de preguntas en el sistema.
@Entity indica que esta clase es una entidad JPA.
@Table especifica el nombre de la tabla en la base de datos.
@Id indica el campo que es la clave primaria.
@GeneratedValue especifica la estrategia de generación de la clave primaria.
*/
@Entity
@Table(name = "CATEGORIAS")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String descripcion;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }


}
