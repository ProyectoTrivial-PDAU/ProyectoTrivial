# ProyectoTrivial

## Status
**Versión:** Inicial  

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
| `PreguntaService.java` | Servicio que carga las preguntas desde el archivo JSON y ofrece métodos de acceso |
| `PreguntaController.java` | Controlador REST que expone los endpoints para obtener preguntas y enviar respuestas |
| `RespuestaDTO.java` | Clase que representa la respuesta enviada por el usuario |
| `preguntas.json` | Archivo con todas las preguntas del juego, ubicado en `src/main/resources` |

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

