package com.example.ProyectoTrivial.Servicios;

import com.example.ProyectoTrivial.Model.Partidas.Partida;
import com.example.ProyectoTrivial.Model.Partidas.PartidaPreguntasID;
import com.example.ProyectoTrivial.Repositorios.PartidaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/*
Clase de servicio para gestionar la lógica relacionada con las partidas.
Utiliza PartidaRepository para realizar operaciones CRUD en las entidades Partida.
@Service indica que esta clase es un servicio de Spring, lo que permite la inyección de dependencias y la gestión del ciclo de vida del bean.
@Autowired se utiliza para inyectar automáticamente la dependencia de PartidaRepository en esta clase.
*/
@Service
public class PartidaService {

    @Autowired
    private PartidaRepository partidaRepository;

    public Partida guardarPartida (Partida partida){

        Partida partidaGuardada = partidaRepository.save(partida);

        if (partida.getPreguntasRespondidas() != null){
            partida.getPreguntasRespondidas().forEach(partidaPreguntas -> {
                partidaPreguntas.setPartida(partidaGuardada);
                if (partidaPreguntas.getId() == null) {
                    partidaPreguntas.setId(new PartidaPreguntasID(
                            partidaGuardada.getId(),
                            partidaPreguntas.getPregunta().getId()
                    ));
                }
                });
            partidaGuardada.setPreguntasRespondidas(partida.getPreguntasRespondidas());
        }
        return partidaRepository.save(partidaGuardada);
    }

    public List<Partida> listarPartidas(){
        return partidaRepository.findAll();
    }


    public Optional<Partida> cargarPartida(Long id){ return partidaRepository.findById(id);}

}
