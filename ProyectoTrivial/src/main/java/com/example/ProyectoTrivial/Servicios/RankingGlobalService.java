package com.example.ProyectoTrivial.Servicios;

import com.example.ProyectoTrivial.Model.Ranking.RankingGlobal;
import com.example.ProyectoTrivial.Repositorios.RankingGlobalRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/*
Clase de servicio para gestionar la lógica relacionada con el ranking global.
Utiliza RankingGlobalRepository para realizar operaciones CRUD en las entidades RankingGlobal.
@Service indica que esta clase es un servicio de Spring, lo que permite la inyección de dependencias y la gestión del ciclo de vida del bean.

*/
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
