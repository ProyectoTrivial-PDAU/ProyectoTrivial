package com.example.ProyectoTrivial.Configuracion;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/*
Clase de configuración para habilitar CORS en la aplicación Spring Boot.
@Configuration indica que esta clase contiene configuraciones de Spring.
*/
@Configuration
public class WebConfig implements WebMvcConfigurer {


    /*
    Método para configurar las políticas de CORS.
    @Override indica que este método sobrescribe un método de la interfaz WebMvcConfigurer.
    CORS (Cross-Origin Resource Sharing) es un mecanismo que permite controlar cómo los recursos de un servidor pueden ser solicitados desde otro dominio.
    1. addMapping("/**"): Aplica la configuración a todas las rutas de la aplicación.
    2. allowedOrigins("*"): Permite solicitudes desde cualquier origen. (Nota: Esto puede ser un riesgo de seguridad en producción).
    3. allowedOrigins("http://localhost:4200", "http://127.0.0.1:5500"): Permite solicitudes desde estos dominios específicos.
    4. allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS"): Permite estos métodos HTTP.
    5. allowedHeaders("*"): Permite todos los encabezados en las solicitudes.
    6. allowCredentials(false): No permite el envío de credenciales (cookies, autenticación HTTP) en las solicitudes CORS.
    */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedOrigins("http://localhost:4200", "http://127.0.0.1:5500")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*") 
                .allowCredentials(false);
    }

}
