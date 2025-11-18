package com.example.ProyectoTrivial.Servicios;

import com.example.ProyectoTrivial.Model.Partidas.Partida;
import com.example.ProyectoTrivial.Repositorios.PartidaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PartidaService {

    @Autowired
    private PartidaRepository partidaRepository;

    public Partida guardarPartida (Partida partida){
        return partidaRepository.save(partida);
    }

    public List<Partida> listarPartidas(){
        return partidaRepository.findAll();
    }


    public Optional<Partida> cargarPartida(Long id){ return partidaRepository.findById(id);}

}
