package com.example.ProyectoTrivial.Repositorios;

import com.example.ProyectoTrivial.Model.Ranking.RankingGlobal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/*
Clase repositorio para la entidad RankingGlobal.
Extiende JpaRepository para proporcionar operaciones CRUD y de consulta para la entidad RankingGlobal.
Esto incluye métodos predefinidos para guardar, eliminar y buscar entidades RankingGlobal en la base de datos.
*/
public interface RankingGlobalRepository extends JpaRepository<RankingGlobal, Long> {

    List<RankingGlobal> findTop20ByOrderByPuntuacionDescFechaAsc();
}
