package com.example.ProyectoTrivial.Servicios;

import com.example.ProyectoTrivial.Preguntas.ContenedorJSON;
import com.example.ProyectoTrivial.Preguntas.Pregunta;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import java.io.InputStream;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

@Service
public class JuegoTrivialService {

    public void jugar() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            InputStream is = getClass().getResourceAsStream("/Preguntas/Preguntas.json");
            ContenedorJSON trivial = mapper.readValue(is, ContenedorJSON.class);

            List<Pregunta> preguntas = trivial.getTrivial();
            Random rand = new Random();
            Scanner sc = new Scanner(System.in);

            int puntuacion = 0;
            boolean acertado = true;

            while (acertado) {
                Pregunta p = preguntas.get(rand.nextInt(preguntas.size()));

                System.out.println("Categoría: " + p.getCategoria());
                System.out.println(p.getPregunta());
                List<String> opciones = p.getOpciones();
                for (int i = 0; i < opciones.size(); i++) {
                    System.out.println((i+1) + ". " + opciones.get(i));
                }

                System.out.print("Introduce el número de tu respuesta: ");
                int respuestaUsuario = sc.nextInt();

                if (opciones.get(respuestaUsuario - 1).equals(p.getRespuesta_correcta())) {
                    System.out.println("¡Correcto!");
                    puntuacion += 10;
                } else {
                    System.out.println("Incorrecto. La respuesta correcta es: " + p.getRespuesta_correcta());
                    acertado = false;
                }
            }

            System.out.println("Puntuación final: " + puntuacion);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
