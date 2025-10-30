package com.example.ProyectoTrivial.Servicios;

import com.example.ProyectoTrivial.Preguntas.Pregunta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * Orquesta una partida: selecciona preguntas, controla el flujo, calcula puntuación
 */
@Service
public class JuegoTrivialService {

    @Autowired
    private PreguntaService preguntaService;

    /**
     * Método para obtener preguntas por categoría
     * @param categoria
     * @param cantidad
     * @return
     */
    public List<Pregunta> obtenerPreguntas(String categoria, int cantidad) {
        return preguntaService.obtenerPorCategoria(categoria, cantidad);
    }

    /**
     * Método para obtener preguntas aleatorias
     * @param cantidad
     * @return
     */
    public List<Pregunta> obtenerPreguntasAleatorias(int cantidad) {
        return preguntaService.obtenerAleatorias(cantidad);
    }
}






/*
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

            List<Pregunta> todasLasPreguntas = trivial.getTrivial();
            Scanner sc = new Scanner(System.in);
            Random rand = new Random();

            boolean seguirJugando = true;

            while (seguirJugando) {
                System.out.println("\nSelecciona el modo de juego:");
                System.out.println("    1. Preguntas aleatorias.");
                System.out.println("    2. Selección por categoría.");
                System.out.print("Introduce el número del modo: ");
                int modo = sc.nextInt();
                sc.nextLine(); // Limpiar buffer

                List<Pregunta> preguntasFiltradas;

                if (modo == 2) {
                    System.out.println("Selecciona una categoría:");
                    System.out.println("1. Matemáticas");
                    System.out.println("2. Geografía");
                    System.out.println("3. Historia");
                    System.out.println("4. Ciencia");
                    System.out.println("5. Arte");
                    System.out.print("Introduce el número de la categoría: ");
                    int opcionCategoria = sc.nextInt();
                    sc.nextLine(); // Limpiar buffer

                    String categoriaSeleccionada = switch (opcionCategoria) {
                        case 1 -> "Matemáticas";
                        case 2 -> "Geografía";
                        case 3 -> "Historia";
                        case 4 -> "Ciencia";
                        case 5 -> "Arte";
                        default -> {
                            System.out.println("Categoría no válida. Se seleccionará una aleatoria.");
                            yield todasLasPreguntas.get(rand.nextInt(todasLasPreguntas.size())).getCategoria();
                        }
                    };

                    preguntasFiltradas = todasLasPreguntas.stream()
                            .filter(p -> p.getCategoria().equalsIgnoreCase(categoriaSeleccionada))
                            .toList();

                    if (preguntasFiltradas.size() < 5) {
                        System.out.println("No hay suficientes preguntas en esa categoría (mínimo 5).");
                        continue;
                    }
                } else {
                    preguntasFiltradas = todasLasPreguntas;
                }

                int puntuacion = 0;

                for (int i = 0; i < 5; i++) {
                    Pregunta p = preguntasFiltradas.get(rand.nextInt(preguntasFiltradas.size()));

                    System.out.println("\nPregunta " + (i + 1));
                    System.out.println("Categoría: " + p.getCategoria());
                    System.out.println(p.getPregunta());
                    List<String> opciones = p.getOpciones();
                    for (int j = 0; j < opciones.size(); j++) {
                        System.out.println((j + 1) + ". " + opciones.get(j));
                    }

                    System.out.print("Introduce el número de tu respuesta: ");
                    int respuestaUsuario = sc.nextInt();

                    if (respuestaUsuario < 1 || respuestaUsuario > opciones.size()) {
                        System.out.println("Respuesta inválida. Se considera incorrecta.");
                    } else if (opciones.get(respuestaUsuario - 1).equals(p.getRespuesta_correcta())) {
                        System.out.println("¡Correcto!");
                        puntuacion += 10;
                    } else {
                        System.out.println("Incorrecto. La respuesta correcta es: " + p.getRespuesta_correcta());
                    }
                }

                System.out.println("\nPuntuación final de la ronda: " + puntuacion);

                System.out.print("\n¿Quieres jugar otra ronda? (s/n): ");
                sc.nextLine(); // ← limpia el salto de línea pendiente
                String respuesta = sc.nextLine().trim().toLowerCase();
                seguirJugando = respuesta.equals("s");

            }

            System.out.println("\nGracias por jugar. ¡Hasta la próxima!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<String> obtenerCategorias() {
        return List.of("Matemáticas", "Geografía", "Historia", "Ciencia", "Arte");
    }

    public List<Pregunta> obtenerPreguntas(String categoria, int cantidad) {
        List<Pregunta> todas = cargarPreguntas();
        List<Pregunta> filtradas = (categoria == null || categoria.isBlank())
                ? todas
                : todas.stream()
                .filter(p -> p.getCategoria().equalsIgnoreCase(categoria))
                .toList();

        Collections.shuffle(filtradas);
        return filtradas.stream().limit(cantidad).toList();
    }
}


*/