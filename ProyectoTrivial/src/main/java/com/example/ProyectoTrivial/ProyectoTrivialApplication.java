package com.example.ProyectoTrivial;

import com.example.ProyectoTrivial.Servicios.JuegoTrivialService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.beans.factory.annotation.Autowired;

@SpringBootApplication
public class ProyectoTrivialApplication implements CommandLineRunner {

    @Autowired
    private JuegoTrivialService juegoTrivialService;

    public static void main(String[] args) {
        SpringApplication.run(ProyectoTrivialApplication.class, args);
    }

    @Override
    public void run(String... args) {
        juegoTrivialService.jugar();
    }
}
