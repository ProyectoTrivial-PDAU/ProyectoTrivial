package com.example.ProyectoTrivial.Controladores;

import com.example.ProyectoTrivial.Model.Partidas.Partida;
import com.example.ProyectoTrivial.Model.Usuarios.Usuario;
import com.example.ProyectoTrivial.Servicios.PartidaService;
import com.example.ProyectoTrivial.Servicios.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/partidas")
@CrossOrigin(origins = "*") // Permite llamadas desde localhost
public class PartidaController {

    @Autowired
    private final PartidaService partidaService;
    private final UsuarioService usuarioService;

    public PartidaController(PartidaService partidaService, UsuarioService usuarioService) {
        this.partidaService = partidaService;
        this.usuarioService = usuarioService;
    }

    // Crear una partida nueva (POST)
    @PostMapping("/usuario/{usuarioId}")
    public Partida guardarPartida(@RequestBody Partida partida, @PathVariable Long usuarioId) {
        Usuario usuario = usuarioService.obtenerUsuarioPorId(usuarioId);
        partida.setUsuario(usuario);
        return partidaService.guardarPartida(partida);
    }

    // Listar todas las partidas (GET)
    @GetMapping
    public List<Partida> getPartidas() {
        return partidaService.listarPartidas();
    }

    // Obtener una partida concreta (GET)
    @GetMapping("/{id}")
    public Optional<Partida> getPartida(@PathVariable Long id) {
        return partidaService.cargarPartida(id);
    }
}