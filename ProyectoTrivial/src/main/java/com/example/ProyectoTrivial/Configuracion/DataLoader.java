package com.example.ProyectoTrivial.Configuracion;

import com.example.ProyectoTrivial.Model.Categoria;
import com.example.ProyectoTrivial.Model.Pregunta;
import com.example.ProyectoTrivial.Model.Respuesta;
import com.example.ProyectoTrivial.Repositorios.CategoriaRepository;
import com.example.ProyectoTrivial.Repositorios.PreguntaRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/*
Clase para cargar datos iniciales en el entorno de desarrollo.
Se ejecuta al iniciar la aplicación si el perfil activo es "dev".
@Component indica que es un bean gestionado por Spring. Un bean es un objeto que forma parte del contenedor de Spring y cuya
vida es gestionada por este.
@Profile("dev") especifica que este bean solo se activa cuando el perfil "dev" está activo.
Implementa ApplicationRunner para ejecutar código después de que la aplicación haya arrancado.

*/
@Component
@Profile("dev")
public class DataLoader implements ApplicationRunner {

    private final CategoriaRepository categoriaRepository;
    private final PreguntaRepository preguntaRepository;

    public DataLoader(CategoriaRepository categoriaRepository, PreguntaRepository preguntaRepository) {
        this.categoriaRepository = categoriaRepository;
        this.preguntaRepository = preguntaRepository;
    }

    /*
    @Override indica que este método sobrescribe un método de la interfaz ApplicationRunner.
    @Transactional asegura que todas las operaciones de la base de datos dentro de este método se ejecuten en una sola transacción.
    */
    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (preguntaRepository.count() > 0) return; // ya poblado

        seedCategoria("Matemáticas", "Preguntas de cálculo y lógica");
        seedCategoria("Geografía", "Países, capitales y ríos");
        seedCategoria("Historia", "Acontecimientos y personajes históricos");
        seedCategoria("Ciencia", "Ciencias naturales y exactas");
        seedCategoria("Arte", "Movimientos y artistas");

        addPregunta("Matemáticas", "¿Cuánto es 12 x 12?",
                Arrays.asList("144","124","132","142"), "144");
        addPregunta("Matemáticas", "¿Cuál es 2^5?",
                Arrays.asList("32","16","64","25"), "32");
        addPregunta("Matemáticas", "¿Cuál es la raíz cuadrada de 81?",
                Arrays.asList("9","8","7","6"), "9");
        addPregunta("Matemáticas", "¿Cuánto es 15 + 28?",
                Arrays.asList("42","43","44","45"), "43");
        addPregunta("Matemáticas", "¿Cuál es el resultado de 100 ÷ 4?",
                Arrays.asList("20","25","30","40"), "25");

        addPregunta("Geografía", "¿Cuál es la capital de Francia?",
                Arrays.asList("Madrid","París","Berlín","Roma"), "París");
        addPregunta("Geografía", "¿Cuál es el país más grande del mundo?",
                Arrays.asList("Canadá","China","Estados Unidos","Rusia"), "Rusia");
        addPregunta("Geografía", "¿Cuál es el río más largo del mundo?",
                Arrays.asList("Amazonas","Nilo","Yangtsé","Misisipi"), "Nilo");
        addPregunta("Geografía", "¿Qué país tiene forma de bota?",
                Arrays.asList("Italia","Grecia","España","Portugal"), "Italia");
        addPregunta("Geografía", "¿Cuál es la capital de Japón?",
                Arrays.asList("Seúl","Beijing","Tokio","Bangkok"), "Tokio");

        addPregunta("Historia", "¿En qué año comenzó la Segunda Guerra Mundial?",
                Arrays.asList("1937","1939","1941","1945"), "1939");
        addPregunta("Historia", "¿Quién fue el primer presidente de Estados Unidos?",
                Arrays.asList("Abraham Lincoln","Thomas Jefferson","George Washington","John Adams"), "George Washington");
        addPregunta("Historia", "¿Qué imperio construyó el Coliseo?",
                Arrays.asList("Griego","Egipcio","Romano","Bizantino"), "Romano");
        addPregunta("Historia", "¿Quién descubrió América?",
                Arrays.asList("Cristóbal Colón","Magallanes","Vasco de Gama","Américo Vespucio"), "Cristóbal Colón");
        addPregunta("Historia", "¿En qué año cayó el Muro de Berlín?",
                Arrays.asList("1987","1988","1989","1990"), "1989");

        addPregunta("Ciencia", "¿Cuál es el planeta más grande del sistema solar?",
                Arrays.asList("Júpiter","Saturno","Neptuno","Marte"), "Júpiter");
        addPregunta("Ciencia", "¿Qué gas respiramos principalmente?",
                Arrays.asList("Oxígeno","Hidrógeno","Nitrógeno","Dióxido de carbono"), "Oxígeno");
        addPregunta("Ciencia", "¿Cuál es la unidad básica de la vida?",
                Arrays.asList("Átomo","Molécula","Célula","Organelo"), "Célula");
        addPregunta("Ciencia", "¿Qué órgano bombea sangre a todo el cuerpo?",
                Arrays.asList("Pulmón","Hígado","Corazón","Riñón"), "Corazón");
        addPregunta("Ciencia", "¿Cuál es el metal más ligero?",
                Arrays.asList("Aluminio","Litio","Oro","Plata"), "Litio");

        addPregunta("Arte", "¿Quién pintó la Mona Lisa?",
                Arrays.asList("Miguel Ángel","Leonardo da Vinci","Pablo Picasso","Vincent van Gogh"), "Leonardo da Vinci");
        addPregunta("Arte", "¿Qué estilo artístico se caracteriza por formas geométricas y colores vivos?",
                Arrays.asList("Impresionismo","Cubismo","Barroco","Romanticismo"), "Cubismo");
        addPregunta("Arte", "¿Quién pintó 'La noche estrellada'?",
                Arrays.asList("Claude Monet","Vincent van Gogh","Salvador Dalí","Frida Kahlo"), "Vincent van Gogh");
        addPregunta("Arte", "¿En qué país nació Frida Kahlo?",
                Arrays.asList("España","México","Italia","Francia"), "México");
        addPregunta("Arte", "¿Qué movimiento artístico es Salvador Dalí famoso por?",
                Arrays.asList("Surrealismo","Cubismo","Impresionismo","Expresionismo"), "Surrealismo");
    }

    /*
    Método para crear una categoría si no existe ya en la base de datos.
    */
    private void seedCategoria(String nombre, String descripcion) {
        categoriaRepository.findByNombre(nombre).orElseGet(() -> {
            Categoria c = new Categoria();
            c.setNombre(nombre);
            c.setDescripcion(descripcion);
            return categoriaRepository.save(c);
        });
    }

    /*
    Metodo para añadir una pregunta con sus respuestas a una categoría existente.
    Busca la categoría por nombre, crea la pregunta y las respuestas, y las guarda en la base de datos.
    */
    private void addPregunta(String categoriaNombre, String texto, List<String> opciones, String correcta) {
        Categoria categoria = categoriaRepository.findByNombre(categoriaNombre)
                .orElseThrow(() -> new IllegalStateException("Categoría no encontrada: " + categoriaNombre));
        Pregunta p = new Pregunta();
        p.setCategoria(categoria);
        p.setPregunta(texto);
        List<Respuesta> respuestas = new ArrayList<>();
        for (String opt : opciones) {
            Respuesta r = new Respuesta();
            r.setTexto(opt);
            r.setEsCorrecta(opt.equals(correcta));
            r.setPregunta(p);
            respuestas.add(r);
        }
        p.setRespuestas(respuestas);
        preguntaRepository.save(p);
    }
}
