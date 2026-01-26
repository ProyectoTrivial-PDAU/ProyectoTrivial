package com.example.ProyectoTrivial.Controladores;

import com.example.ProyectoTrivial.Model.Ranking.RankingGlobal;
import com.example.ProyectoTrivial.Servicios.RankingGlobalService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trivial")
@CrossOrigin(origins = "*")
public class RankingGlobalController {

    private final RankingGlobalService service;

    public RankingGlobalController(RankingGlobalService service) {
        this.service = service;
    }

    @PostMapping("/ranking-global")
    public RankingGlobal guardar(@RequestBody RankingGlobal partida) {
        return service.guardar(partida);
    }

    @GetMapping("/ranking-global")
    public List<RankingGlobal> obtenerRanking() {
        return service.obtenerRanking();
    }
}
