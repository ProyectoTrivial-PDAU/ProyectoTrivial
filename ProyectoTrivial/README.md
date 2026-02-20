# ⚙️ Brainiak Backend - Spring Boot

Backend de la aplicación Brainiak desarrollado con **Spring Boot 3.x**.

## 📁 Estructura del Proyecto

```
src/main/java/com/example/ProyectoTrivial/
├── Configuracion/
│   ├── WebConfig.java             # Configuración CORS
│   └── DataLoader.java            # Carga de datos iniciales
│
├── Controladores/
│   ├── JuegoTrivialController.java  # API de preguntas
│   └── CategoriaController.java     # API de categorías
│
├── Model/
│   ├── Pregunta.java              # Entidad JPA de pregunta
│   ├── Categoria.java             # Entidad JPA de categoría
│   └── Usuarios/
│       └── Usuario.java           # Entidad JPA de usuario
│
├── Repositorios/
│   ├── PreguntaRepository.java    # Repository de preguntas
│   ├── CategoriaRepository.java   # Repository de categorías
│   └── UsuarioRepository.java     # Repository de usuarios
│
├── Servicios/
│   ├── PreguntaService.java       # Lógica de preguntas
│   ├── JuegoTrivialService.java   # Lógica del juego
│   └── UsuarioService.java        # Lógica de usuarios
│
├── Preguntas/
│   └── Pregunta.java              # DTO para respuestas API
│
└── ProyectoTrivialApplication.java  # Clase principal
```

---

## 🚀 Comandos

### Requisitos
- **Java 17+**
- **Maven 3.x**

### Compilar
```bash
./mvnw clean compile
```

### Ejecutar (desarrollo)
```bash
./mvnw spring-boot:run
```

### Ejecutar con perfil específico
```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

### Empaquetar
```bash
./mvnw clean package -DskipTests
```

### Ejecutar JAR
```bash
java -jar target/ProyectoTrivial-0.0.1-SNAPSHOT.jar
```

### Tests
```bash
./mvnw test
```

---

## 📡 API REST

### Base URL
```
http://localhost:8080/api
```

### Endpoints de Preguntas

#### Obtener preguntas aleatorias
```http
GET /trivial/preguntas?cantidad=5
```

**Respuesta:**
```json
[
  {
    "categoria": "Historia",
    "pregunta": "¿En qué año comenzó la Segunda Guerra Mundial?",
    "opciones": ["1935", "1939", "1941", "1945"],
    "respuesta_correcta": "1939"
  }
]
```

#### Obtener preguntas por categoría
```http
GET /trivial/preguntas?categoria=Historia&cantidad=5
```

### Endpoints de Categorías

#### Listar categorías
```http
GET /trivial/categorias
```

**Respuesta:**
```json
["Arte", "Ciencia", "Geografía", "Historia", "Matemáticas"]
```

### Endpoints de Usuarios

#### Listar usuarios
```http
GET /usuarios
```

#### Crear usuario
```http
POST /usuarios
Content-Type: application/json

{
  "name": "Juan",
  "nickname": "juanito"
}
```

#### Eliminar usuario
```http
DELETE /usuarios/{id}
```

---

## 🗄️ Base de Datos

### Perfil de Desarrollo (H2)

Base de datos en memoria con datos precargados.

**Configuración** (`application-dev.properties`):
```properties
spring.datasource.url=jdbc:h2:mem:trivialdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.h2.console.enabled=true
spring.jpa.hibernate.ddl-auto=create-drop
```

**Consola H2:** http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:mem:trivialdb`
- User: `sa`
- Password: (vacío)

### Perfil de Producción (MariaDB/MySQL)

Configurar en `application-prod.properties`:
```properties
spring.datasource.url=jdbc:mariadb://localhost:3306/trivialdb
spring.datasource.username=usuario
spring.datasource.password=contraseña
spring.jpa.hibernate.ddl-auto=update
```

---

## 🏗️ Entidades JPA

### Categoria
```java
@Entity
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nombre;
    
    @OneToMany(mappedBy = "categoria")
    private List<Pregunta> preguntas;
}
```

### Pregunta
```java
@Entity
public class Pregunta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String textoPregunta;
    private String opcion1;
    private String opcion2;
    private String opcion3;
    private String opcion4;
    private String respuestaCorrecta;
    
    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;
}
```

### Usuario
```java
@Entity
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String email;
    private String password;
    private String nombre_usuario;
}
```

---

## 🔧 Configuración

### application.properties
```properties
# Puerto del servidor
server.port=8080

# Perfil activo
spring.profiles.active=dev

# Nombre de la aplicación
spring.application.name=ProyectoTrivial
```

### CORS (WebConfig.java)
```java
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
            .allowedOrigins("http://localhost:4200")
            .allowedMethods("GET", "POST", "PUT", "DELETE")
            .allowedHeaders("*");
    }
}
```

---

## 📦 Datos Iniciales (DataLoader)

El `DataLoader.java` carga datos automáticamente en el perfil `dev`:

### Categorías
- Arte
- Ciencia
- Geografía
- Historia
- Matemáticas

### Preguntas
25 preguntas distribuidas (5 por categoría):
- Preguntas de cultura general
- 4 opciones por pregunta
- Respuesta correcta marcada

---

## 🔌 Servicios

### PreguntaService
- `obtenerTodasLasPreguntas()` - Lista todas las preguntas
- `obtenerPreguntasPorCategoria(String nombre)` - Filtra por categoría

### JuegoTrivialService
- `obtenerPreguntasAleatorias(int cantidad)` - Preguntas aleatorias
- `obtenerPreguntasPorCategoria(String categoria, int cantidad)` - Por categoría

### UsuarioService
- `registrarUsuario(String email, String password, String nombreUsuario)`
- `loginUsuario(String email, String password)`
- `obtenerUsuarioPorId(Long id)`

---

## 🧪 Testing

### Estructura de tests
```
src/test/
├── java/com/example/ProyectoTrivial/
│   └── ProyectoTrivialApplicationTests.java
└── resources/
    └── Preguntas/
        └── Preguntas.json          # Datos de prueba
```

### Ejecutar tests
```bash
./mvnw test
```

---

## 📋 Dependencias (pom.xml)

```xml
<dependencies>
    <!-- Spring Boot Starters -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    
    <!-- Base de datos -->
    <dependency>
        <groupId>com.h2database</groupId>
        <artifactId>h2</artifactId>
        <scope>runtime</scope>
    </dependency>
    
    <!-- Lombok (opcional) -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>
    
    <!-- Testing -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```

---

## 🔄 CI/CD

El proyecto incluye un `Jenkinsfile` en la raíz para integración continua.

---

## 📄 Licencia

Proyecto educativo - DAM2 2025/2026
