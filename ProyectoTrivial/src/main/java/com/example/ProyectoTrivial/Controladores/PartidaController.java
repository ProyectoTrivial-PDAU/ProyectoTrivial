package com.example.ProyectoTrivial.Controladores;

import com.example.ProyectoTrivial.Partidas.Partida;
import com.example.ProyectoTrivial.Servicios.PartidaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trivial")
@CrossOrigin(origins = "*")
public class PartidaController {

    @Autowired
    private PartidaService partidaService;

    @PostMapping("/partida")
    public  Partida registrarPartida (@RequestBody Partida partida){
        return partidaService.guardarPartida(partida);
    }

    @GetMapping("/partidas")
    public List<Partida> obtenerPartidas(){
        return partidaService.listarPartidas();
    }


}
