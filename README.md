# ProyectoTrivial

Aplicación web desarrollada utilizando el framework Spring Boot que simula un juego de preguntas tipo trivial. Las preguntas y respuestas están almacenadas en un archivo JSON y se exponen mediante una API REST para ser consultadas y respondidas desde cualquier cliente.


1. Descripción
Este proyecto permite al usuario jugar a un juego del estilo Trivial, en la que se sucederán preguntas aleatorias de distintos ámbitos, con un récord de puntuación con cada partida, así como un ranking de mejores puntuaciones. El juego mostrará preguntas aleatorias hasta que el jugador falle la primera, momento en el que la partida acabará y se registrará su puntuación.

2.- Estructura del proyecto
- Pregunta.java: clase modelo que representa una pregunta del trivial
- PreguntaService.java: servicio que carga las preguntas desde el archivo JSON y ofrece métodos para acceder a ellas
- PreguntaController.java: controlador REST que expone los endpoints para obtener preguntas y enviar respuestas
- RespuestaDTO.java: clase que representa la respuesta enviada por el usuario
- preguntas.json: archivo con todas las preguntas del juego, ubicado en src/main/resources

3.- Requisitos
- Java 17 o superior
- Maven
- IDE: IntelliJ
- MariaDB
  
4.- Ejecución
- Clonar el repositorio:
git clone https://github.com/usuario/trivial-spring.git
- Abrir el proyecto en tu IDE
- Ejecutar la aplicación:
mvn spring-boot:run
- Acceder a los endpoints:
- GET /api/preguntas: lista todas las preguntas
- GET /api/preguntas/aleatoria: devuelve una pregunta aleatoria
- POST /api/preguntas/responder: recibe una respuesta y devuelve si es correcta

5.- Tecnologías utilizadas
- Java
- Spring Boot
- Maven
- Jackson (para leer el archivo JSON)
- API REST
Ejemplo de uso
Obtener una pregunta
GET /api/preguntas/aleatoria

6.- Potenciales mejoras futuras
- Persistencia en base de datos
- Interfaz web con HTML y JavaScript
- Modo multijugador
- Selector de dificultad
- Selector de categoria de preguntas
