package com.example.ProyectoTrivial;

import com.example.ProyectoTrivial.Pregunta;
import com.example.ProyectoTrivial.ContenedorJSON;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class JuegoTrivial {

    public static void main(String[] args) {
        try {
            // Leer JSON
            ObjectMapper mapper = new ObjectMapper();
            InputStream is = JuegoTrivial.class.getResourceAsStream("/Preguntas/Preguntas.json");
            ContenedorJSON trivial = mapper.readValue(is, ContenedorJSON.class);

            List<Pregunta> preguntas = trivial.getTrivial();

            // Elegir una pregunta al azar
            Random rand = new Random();
            Pregunta p = preguntas.get(rand.nextInt(preguntas.size()));

            // Mostrar pregunta y opciones
            System.out.println("Categoría: " + p.getCategoria());
            System.out.println(p.getPregunta());
            List<String> opciones = p.getOpciones();
            for (int i = 0; i < opciones.size(); i++) {
                System.out.println((i+1) + ". " + opciones.get(i));
            }

            // Leer respuesta del usuario
            Scanner sc = new Scanner(System.in);
            System.out.print("Introduce el número de tu respuesta: ");
            int respuestaUsuario = sc.nextInt();

            // Comprobar respuesta
            if (opciones.get(respuestaUsuario - 1).equals(p.getRespuesta_correcta())) {
                System.out.println("¡Correcto!");
            } else {
                System.out.println("Incorrecto. La respuesta correcta es: " + p.getRespuesta_correcta());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

