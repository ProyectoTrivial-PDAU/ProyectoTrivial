package com.example.ProyectoTrivial;

import com.example.ProyectoTrivial.Model.Categoria;
import com.example.ProyectoTrivial.Model.Pregunta;
import com.example.ProyectoTrivial.Model.Respuesta;
import com.example.ProyectoTrivial.Model.Usuarios.Usuario;
import com.example.ProyectoTrivial.Repositorios.CategoriaRepository;
import com.example.ProyectoTrivial.Repositorios.PreguntaRepository;
import com.example.ProyectoTrivial.Repositorios.UsuarioRepository;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/*
 * Configuración compartida para todos los tests.
 *
 * Se encarga de:
 *  - Proveer un PasswordEncoder real para los tests de usuario.
 *  - Proveer un RestTemplate real (los tests que necesiten mock lo sobreescriben con @MockBean).
 *
 * Criterio a): "Se han configurado y estructurado diferentes pruebas de la aplicación"
 *    Esta clase centraliza la configuración del entorno de test,
 *    evitando repetición de @Bean en cada clase y facilitando
 *    el mantenimiento del conjunto de pruebas.
 */
@TestConfiguration
public class TestConfig {

    @Bean
    @Primary
    public PasswordEncoder testPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Primary
    public RestTemplate testRestTemplate() {
        return new RestTemplate();
    }


    /*
     * Crea y persiste una Categoria en la BD de test.
     */

    public static Categoria crearCategoria(CategoriaRepository repo, String nombre) {
        Categoria c = new Categoria();
        c.setNombre(nombre);
        c.setDescripcion("Descripción de " + nombre);
        return repo.save(c);
    }

    /*
     * Crea y persiste una Pregunta con cuatro respuestas (una correcta).
     */

    public static Pregunta crearPreguntaCompleta(PreguntaRepository repo,
                                                  Categoria categoria,
                                                  String textoPregunta,
                                                  String textoCorrecta) {
        Pregunta p = new Pregunta();
        p.setCategoria(categoria);
        p.setPregunta(textoPregunta);

        Respuesta correcta = new Respuesta();
        correcta.setTexto(textoCorrecta);
        correcta.setEsCorrecta(true);
        correcta.setPregunta(p);

        Respuesta incorrecta1 = new Respuesta();
        incorrecta1.setTexto("Opción incorrecta 1");
        incorrecta1.setEsCorrecta(false);
        incorrecta1.setPregunta(p);

        Respuesta incorrecta2 = new Respuesta();
        incorrecta2.setTexto("Opción incorrecta 2");
        incorrecta2.setEsCorrecta(false);
        incorrecta2.setPregunta(p);

        Respuesta incorrecta3 = new Respuesta();
        incorrecta3.setTexto("Opción incorrecta 3");
        incorrecta3.setEsCorrecta(false);
        incorrecta3.setPregunta(p);

        p.setRespuestas(List.of(correcta, incorrecta1, incorrecta2, incorrecta3));
        return repo.save(p);
    }

    /*
     * Crea y persiste un Usuario con email y password dados.
     */


    public static Usuario crearUsuario(UsuarioRepository repo,
                                        PasswordEncoder encoder,
                                        String email,
                                        String password,
                                        String nombre) {
        Usuario u = new Usuario();
        u.setEmail(email);
        u.setPassword(encoder.encode(password));
        u.setNombre_usuario(nombre);
        return repo.save(u);
    }
}
