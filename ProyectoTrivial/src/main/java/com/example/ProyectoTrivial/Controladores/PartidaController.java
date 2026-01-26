package com.example.ProyectoTrivial.Controladores;

import com.example.ProyectoTrivial.Model.Partidas.Partida;
import com.example.ProyectoTrivial.Model.Usuarios.Usuario;
import com.example.ProyectoTrivial.Servicios.PartidaService;
import com.example.ProyectoTrivial.Servicios.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/*
Clase controladora para manejar las solicitudes relacionadas con las partidas de juego.
@RestController indica que esta clase es un controlador REST.
@RequestMapping("/partidas") define la ruta base para todas las solicitudes manejadas por este controlador.
@CrossOrigin(origins = "*") habilita CORS para este controlador, permitiendo solicitudes desde cualquier origen.
*/

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

    /*
     * Metodo para guardar una nueva partida asociada a un usuario.
     * 
     * @PostMapping("/usuario/{usuarioId}") indica que este método maneja las
     * solicitudes POST a la ruta /usuario/{usuarioId}.
     * 
     * @RequestBody Partida partida indica que el cuerpo de la solicitud se mapea a
     * un objeto Partida.
     * 
     * @PathVariable Long usuarioId indica que el valor de usuarioId se extrae de la
     * ruta de la solicitud.
     * 
     * @return La partida guardada.
     */
    @PostMapping("/usuario/{usuarioId}")
    public Partida guardarPartida(@RequestBody Partida partida, @PathVariable Long usuarioId) {
        Usuario usuario = usuarioService.obtenerUsuarioPorId(usuarioId);
        partida.setUsuario(usuario);
        return partidaService.guardarPartida(partida);
    }

    /*
     * Metodo para obtener todas las partidas guardadas.
     * 
     * @GetMapping indica que este método maneja las solicitudes GET a la ruta base
     * /partidas.
     * 
     * @return Lista de todas las partidas.
     */
    @GetMapping
    public List<Partida> getPartidas() {
        return partidaService.listarPartidas();
    }

    /*
     * Metodo para obtener una partida por su ID.
     * 
     * @GetMapping("/{id}") indica que este método maneja las solicitudes GET a la
     * ruta /{id}.
     * 
     * @PathVariable Long id indica que el valor de id se extrae de la ruta de la
     * solicitud.
     * 
     * @return La partida correspondiente al ID proporcionado, envuelta en un
     * Optional.
     */
    @GetMapping("/{id}")
    public Optional<Partida> getPartida(@PathVariable Long id) {
        return partidaService.cargarPartida(id);
    }
}