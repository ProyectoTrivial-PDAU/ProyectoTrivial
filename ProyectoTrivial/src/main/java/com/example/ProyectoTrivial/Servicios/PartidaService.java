package com.example.ProyectoTrivial.Servicios;

import com.example.ProyectoTrivial.Partidas.Partida;
import com.example.ProyectoTrivial.Repositorios.PartidaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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


}
