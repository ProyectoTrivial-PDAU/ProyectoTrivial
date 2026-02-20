package com.example.ProyectoTrivial.Controladores;

import com.example.ProyectoTrivial.Model.Ranking.RankingGlobal;
import com.example.ProyectoTrivial.Servicios.RankingGlobalService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
Clase controladora para manejar las solicitudes relacionadas con el ranking global del trivial.
@RestController indica que esta clase es un controlador REST.
*/
@RestController
@RequestMapping("/api/trivial")
@CrossOrigin(origins = "*")
public class RankingGlobalController {

    private final RankingGlobalService service;

    public RankingGlobalController(RankingGlobalService service) {
        this.service = service;
    }

    /*
     * Metodo para guardar una nueva entrada en el ranking global.
     * 
     * @PostMapping("/ranking-global") indica que este método maneja las solicitudes
     * POST a la ruta /ranking-global.
     * 
     * @RequestBody RankingGlobal partida indica que el cuerpo de la solicitud se
     * mapea a un objeto RankingGlobal.
     * 
     * @return La entrada guardada en el ranking global.
     */
    @PostMapping("/ranking-global")
    public RankingGlobal guardar(@RequestBody RankingGlobal partida) {
        return service.guardar(partida);
    }

    /*
     * Metodo para obtener el ranking global.
     * 
     * @GetMapping("/ranking-global") indica que este método maneja las solicitudes
     * GET a la ruta /ranking-global.
     * 
     * @return Lista de entradas del ranking global.
     */
    @GetMapping("/ranking-global")
    public List<RankingGlobal> obtenerRanking() {
        return service.obtenerRanking();
    }
}
