package com.example.ProyectoTrivial.Servicios;

import com.example.ProyectoTrivial.Model.Ranking.RankingGlobal;
import com.example.ProyectoTrivial.Repositorios.RankingGlobalRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RankingGlobalService {

    private final RankingGlobalRepository repo;

    public RankingGlobalService(RankingGlobalRepository repo) {
        this.repo = repo;
    }

    public RankingGlobal guardar(RankingGlobal partida) {
        return repo.save(partida);
    }

    public List<RankingGlobal> obtenerRanking() {
        return repo.findTop20ByOrderByPuntuacionDescFechaAsc();
    }
}
