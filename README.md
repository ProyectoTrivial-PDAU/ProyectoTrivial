# Brainiak

[![Status](https://img.shields.io/badge/Status-In%20Development-yellow)](https://github.com/dIAgnoseTeam/dIAgnose)
[![Version](https://img.shields.io/badge/Version-1.0-green)](https://github.com/dIAgnoseTeam/dIAgnose)

Aplicación web desarrollada con **Spring Boot** que simula un juego tipo **Trivial**. Las preguntas y respuestas se almacenan en un archivo **JSON** y se exponen mediante una **API REST**, permitiendo que cualquier cliente pueda consultar y responder.

---

## Descripción
El proyecto permite a los usuarios jugar a un juego de preguntas y respuestas de distintas categorías.  
- Existen dos modos de juego: preguntas aleatorias o preguntas por categoría.
- En cada ronda se lanzan cinco preguntas.
- Se registra la puntuación de cada partida.  
- Se mantiene un **ranking** de mejores puntuaciones. (Funcionalidad en producción)

---

## Estructura del proyecto

```
proyecto-trivial/
├── proyecto-trivial-frontend/
│   ├── js/
│   │   └── app.js             # Lógica principal del juego: navegación, preguntas, puntuación
│   ├── styles/
│   │   ├── style.css          # Estilos visuales del juego (modo claro/oscuro, layout)
│   └── index.html             # Estructura HTML del juego (pantallas, botones, contenedores)
│   
│
└── ProyectoTrivial/           # Backend en Spring Boot
    └── src/
        └── main/
            └── java/com/example/ProyectoTrivial/
                ├── Configuracion/
                │   └── WebConfig.java
                ├── Controladores/
                │   ├── CategoriaController.java
                │   └── JuegoTrivialController.java
                ├── Preguntas/
                │   ├── ContenedorJSON.java 
                │   └── Pregunta.java
                ├── Servicios/
                │   ├── PreguntaService.java
                │   └── JuegoTrivialService.java
                └── ProyectoTrivialApplication.java
    
```


| Archivo/Clase Java | Descripción |
|---------------|-------------|
| `WebConfig.java` | Configuración CORS para permitir acceso desde el frontend |
| `CategoriaController.java` | Endpoint para obtener categorías únicas |
| `JuegoTrivialController.java` | Endpoint para obtener preguntas (aleatorias o por categoría) |
| `ContenedorJSON.java` | Clase encargada de leer el archivo JSON que contiene las preguntas |
| `Pregunta.java` | Clase modelo que representa una pregunta del trivial |
| `PreguntaService.java` | Lógica para cargar y filtrar preguntas desde el JSON |
| `JuegoTrivialService.java` | Orquestación del juego (flujo, puntuación, selección) |
| `ProyectoTrivialApplication.java` | Clase principal que arranca la aplicación Spring Boot |

---

## Requisitos

- **Java 17** o superior
- **Maven**  
- **IDE Backend:** IntelliJ IDEA
- **IDE Frontend:** Visual Studio  
- **Base de datos:** MariaDB (para futuras mejoras)  

---

## Ejecución

1. Clonar el repositorio:

```bash
git clone https://github.com/ProyectoTrivial-PDAU/ProyectoTrivial.git

