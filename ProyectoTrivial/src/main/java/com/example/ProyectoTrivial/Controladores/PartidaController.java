package com.example.ProyectoTrivial.Controladores;

import com.example.ProyectoTrivial.Model.Partidas.Partida;
import com.example.ProyectoTrivial.Servicios.PartidaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trivial")
@CrossOrigin(origins = "*")
public class PartidaController {

    @Autowired
    private PartidaService partidaService;


    /**
    * Método para guardar la partida
    * @param partida -> objeto de clase "Partida"
     * @return La partida guardada con el ID asignado a la partida


     */
    @PostMapping("/partida")
    public  Partida registrarPartida (@RequestBody Partida partida){
        return partidaService.guardarPartida(partida);
    }


    /**
     *
     * @return lista de todas las partidas
     */

    @GetMapping("/partidas")
    public List<Partida> obtenerPartidas(){
        return partidaService.listarPartidas();
    }


    /**
     *
     * @param id id de la URL.
     * @PathVariable extrae el ID de la URL y lo pasa como parámetro
     * partidaService.cargarPartida(id) llama al servicio y busca una partida con ese ID. Devuelve optional.
     * .map: en caso de existir, envuelve la partida en una respuesta HTTP (200 OK) CON EL CUERPO JSON
     * .orElse: en caso de NO existir, envuelve la partida en una respuesta HTTP (404 NOT FOUND) SIN CUERPO
     * @return
     */
    @GetMapping("/partidas/{id}")
    public ResponseEntity<Partida> cargarPartidasPorID(@PathVariable Long id){
        return partidaService.cargarPartida(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());

    }

}
