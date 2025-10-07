# ProyectoTrivial

[![Status](https://img.shields.io/badge/Status-In%20Development-yellow)](https://github.com/dIAgnoseTeam/dIAgnose)
[![Version](https://img.shields.io/badge/Version-1.0-green)](https://github.com/dIAgnoseTeam/dIAgnose)

Aplicación web desarrollada con **Spring Boot** que simula un juego tipo **Trivial**. Las preguntas y respuestas se almacenan en un archivo **JSON** y se exponen mediante una **API REST**, permitiendo que cualquier cliente pueda consultar y responder.

---

## Descripción
El proyecto permite a los usuarios jugar a un trivial con preguntas aleatorias de distintos ámbitos.  
- El juego continúa hasta que el jugador falla una pregunta, momento en el que la partida termina.  
- Se registra la puntuación de cada partida.  
- Se mantiene un **ranking** de mejores puntuaciones.

---

## Estructura del proyecto

| Archivo/Clase | Descripción |
|---------------|-------------|
| `Pregunta.java` | Clase modelo que representa una pregunta del trivial |
| `JuegoTrivial.java` | Clase que representa el juego, elige las preguntas, las muestra y lee y comprueba las respuestas del usuario |
| `ContenedorJSON.java` | Clase encargada de leer el archivo JSON que contiene las preguntas |
| `ProyectoTrivialApplication.java` | Clase que inicia la aplicación |

---

## Requisitos

- **Java 17** o superior  
- **Maven**  
- **IDE:** IntelliJ IDEA  
- **Base de datos:** MariaDB (para futuras mejoras)  

---

## Ejecución

1. Clonar el repositorio:

```bash
git clone https://github.com/ProyectoTrivial-PDAU/ProyectoTrivial

