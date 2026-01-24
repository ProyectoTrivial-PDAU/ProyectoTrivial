package com.example.ProyectoTrivial.Repositorios;

import com.example.ProyectoTrivial.Model.Ranking.RankingGlobal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RankingGlobalRepository extends JpaRepository<RankingGlobal, Long> {

    List<RankingGlobal> findTop20ByOrderByPuntuacionDescFechaAsc();
}
