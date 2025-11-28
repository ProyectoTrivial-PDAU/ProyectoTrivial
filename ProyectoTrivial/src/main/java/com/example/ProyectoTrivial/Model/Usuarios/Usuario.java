package com.example.ProyectoTrivial.Model.Usuarios;

import com.example.ProyectoTrivial.Model.Partidas.Partida;
import jakarta.persistence.*;

import java.util.List;


@Entity
@Table(name = "USUARIOS")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "EMAIL", nullable = false, unique = true)
    private String email;

    @Column(name = "PASSWORD_HASH", nullable = false)
    private String password;

    @Column(name = "NOMBRE_US", nullable = false)
    private String nombre_usuario;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Partida> partidas;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNombre_usuario() {
        return nombre_usuario;
    }

    public void setNombre_usuario(String nombre_usuario) {
        this.nombre_usuario = nombre_usuario;
    }

    public List<Partida> getPartidas() {
        return partidas;
    }
    public void setPartidas(List<Partida> partidas) {
        this.partidas = partidas;
    }
}
