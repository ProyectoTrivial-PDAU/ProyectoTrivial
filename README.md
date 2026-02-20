# 🧠 Brainiak - Juego de Trivia

[![Status](https://img.shields.io/badge/Status-Production-green)](https://github.com/dIAgnoseTeam/dIAgnose)
[![Version](https://img.shields.io/badge/Version-2.0-blue)](https://github.com/dIAgnoseTeam/dIAgnose)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen)](https://spring.io/projects/spring-boot)
[![Angular](https://img.shields.io/badge/Angular-19-red)](https://angular.io/)

Aplicación web de juego tipo **Trivial** desarrollada con **Spring Boot** (backend) y **Angular** (frontend). Las preguntas se almacenan en base de datos **H2** y se exponen mediante una **API REST**.

---

## 📋 Descripción

Brainiak permite a los usuarios jugar a un juego de preguntas y respuestas de distintas categorías:

- 🎮 **Dos modos de juego**: preguntas aleatorias o por categoría
- ❓ **5 preguntas** por ronda
- 🏆 **Sistema de ranking** local
- 🌙 **Tema oscuro/claro** 
- 👤 **Gestión de perfil** de usuario

---

## 🏗️ Arquitectura del Proyecto

```
ProyectoTrivial/
├── 📂 brainiak-frontend/          # Frontend Angular 19
│   ├── src/app/
│   │   ├── components/            # Componentes UI
│   │   │   ├── sidebar/           # Navegación lateral
│   │   │   ├── header/            # Cabecera con logo
│   │   │   ├── home/              # Pantalla principal
│   │   │   ├── game/              # Juego de preguntas
│   │   │   ├── results/           # Pantalla de resultados
│   │   │   ├── category-selection/# Selector de categorías
│   │   │   ├── ranking/           # Tabla de ranking
│   │   │   ├── users/             # Gestión de usuarios
│   │   │   ├── profile-modal/     # Modal de perfil
│   │   │   └── toast/             # Notificaciones
│   │   ├── services/              # Servicios Angular
│   │   │   ├── trivial.ts         # API preguntas
│   │   │   ├── user.ts            # Gestión usuarios
│   │   │   ├── theme.ts           # Tema oscuro/claro
│   │   │   └── toast.ts           # Notificaciones
│   │   └── models/                # Interfaces TypeScript
│   │       ├── pregunta.ts
│   │       └── user.ts
│   └── package.json
│
├── 📂 ProyectoTrivial/            # Backend Spring Boot
│   ├── src/main/java/com/example/ProyectoTrivial/
│   │   ├── Configuracion/
│   │   │   ├── WebConfig.java     # CORS config
│   │   │   └── DataLoader.java    # Carga datos iniciales
│   │   ├── Controladores/
│   │   │   ├── JuegoTrivialController.java
│   │   │   └── CategoriaController.java
│   │   ├── Model/
│   │   │   ├── Pregunta.java      # Entidad JPA
│   │   │   ├── Categoria.java     # Entidad JPA
│   │   │   └── Usuarios/
│   │   │       └── Usuario.java   # Entidad JPA
│   │   ├── Repositorios/
│   │   │   ├── PreguntaRepository.java
│   │   │   ├── CategoriaRepository.java
│   │   │   └── UsuarioRepository.java
│   │   └── Servicios/
│   │       ├── PreguntaService.java
│   │       ├── JuegoTrivialService.java
│   │       └── UsuarioService.java
│   ├── src/main/resources/
│   │   ├── application.properties
│   │   └── application-dev.properties  # Config H2
│   └── pom.xml
│
├── 📄 BBDDTrivial.sql             # Script SQL (referencia)
├── 📄 Jenkinsfile                 # CI/CD pipeline
└── 📄 README.md                   # Esta documentación
```

---

## 🛠️ Tecnologías

### Backend
| Tecnología | Versión | Uso |
|------------|---------|-----|
| Java | 17+ | Lenguaje principal |
| Spring Boot | 3.x | Framework backend |
| Spring Data JPA | - | Persistencia |
| H2 Database | - | Base de datos en memoria (dev) |
| Maven | 3.x | Gestión de dependencias |

### Frontend
| Tecnología | Versión | Uso |
|------------|---------|-----|
| Angular | 19 | Framework frontend |
| TypeScript | 5.x | Lenguaje tipado |
| SCSS | - | Estilos |
| RxJS | - | Programación reactiva |

---

## 🚀 Instalación y Ejecución

### Prerrequisitos
- **Java 17+**
- **Node.js 20+** (para el frontend)
- **Maven 3.x**

### 1. Clonar el repositorio
```bash
git clone <url-del-repositorio>
cd ProyectoTrivial
```

### 2. Ejecutar el Backend
```bash
cd ProyectoTrivial
./mvnw spring-boot:run
```
El backend estará disponible en: `http://localhost:8080`

### 3. Ejecutar el Frontend
```bash
cd brainiak-frontend
npm install
ng serve --port 4200
```
El frontend estará disponible en: `http://localhost:4200`

---

## 📡 API REST

### Endpoints disponibles

#### Preguntas
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/api/trivial/preguntas?cantidad=5` | Obtener preguntas aleatorias |
| `GET` | `/api/trivial/preguntas?categoria=Historia&cantidad=5` | Preguntas por categoría |

#### Categorías
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/api/trivial/categorias` | Listar todas las categorías |

#### Usuarios
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/api/usuarios` | Listar usuarios |
| `POST` | `/api/usuarios` | Crear usuario |
| `DELETE` | `/api/usuarios/{id}` | Eliminar usuario |

### Ejemplo de respuesta - Preguntas
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

### Ejemplo de respuesta - Categorías
```json
["Arte", "Ciencia", "Geografía", "Historia", "Matemáticas"]
```

---

## 🎮 Modos de Juego

### Modo Clásico
- Preguntas aleatorias de todas las categorías
- 5 preguntas por partida
- Puntuación al final

### Modo Por Categoría
- Selecciona una categoría específica
- 5 preguntas de esa categoría
- Ideal para practicar temas específicos

---

## 🎨 Características del Frontend

### Componentes principales

| Componente | Descripción |
|------------|-------------|
| `Sidebar` | Navegación lateral con iconos |
| `Header` | Logo y perfil de usuario |
| `Home` | Pantalla de inicio con modos de juego |
| `Game` | Pantalla de juego con preguntas |
| `Results` | Pantalla de resultados finales |
| `CategorySelection` | Grid de categorías disponibles |
| `Ranking` | Tabla de mejores puntuaciones |
| `ProfileModal` | Modal para editar perfil |
| `Toast` | Sistema de notificaciones |

### Servicios

| Servicio | Responsabilidad |
|----------|-----------------|
| `TrivialService` | Comunicación con API de preguntas |
| `UserService` | Gestión de usuarios y rankings |
| `ThemeService` | Toggle tema oscuro/claro |
| `ToastService` | Mostrar notificaciones |

---

## 🗄️ Base de Datos

### Entidades JPA

#### Categoria
```java
@Entity
public class Categoria {
    @Id @GeneratedValue
    private Long id;
    private String nombre;
}
```

#### Pregunta
```java
@Entity
public class Pregunta {
    @Id @GeneratedValue
    private Long id;
    private String textoPregunta;
    private String opcion1, opcion2, opcion3, opcion4;
    private String respuestaCorrecta;
    
    @ManyToOne
    private Categoria categoria;
}
```

#### Usuario
```java
@Entity
public class Usuario {
    @Id @GeneratedValue
    private Long id;
    private String email;
    private String password;
    private String nombre_usuario;
}
```

### Datos iniciales (DataLoader)

El `DataLoader.java` carga automáticamente 5 categorías y 25 preguntas de ejemplo al iniciar la aplicación en perfil `dev`.

---

## ⚙️ Configuración

### application.properties
```properties
spring.profiles.active=dev
```

### application-dev.properties (H2 en memoria)
```properties
spring.datasource.url=jdbc:h2:mem:trivialdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.h2.console.enabled=true
spring.jpa.hibernate.ddl-auto=create-drop
```

Consola H2 disponible en: `http://localhost:8080/h2-console`

---

## 🧪 Testing

### Backend
```bash
cd ProyectoTrivial
./mvnw test
```

### Frontend
```bash
cd brainiak-frontend
ng test
```

---

## 📦 Build para Producción

### Backend
```bash
cd ProyectoTrivial
./mvnw clean package -DskipTests
java -jar target/ProyectoTrivial-0.0.1-SNAPSHOT.jar
```

### Frontend
```bash
cd brainiak-frontend
ng build --configuration production
```
Los archivos estáticos se generan en `dist/brainiak-frontend/`

---

## 🔄 CI/CD

El proyecto incluye un `Jenkinsfile` para integración continua.

---

## 👥 Autores

- **Equipo BrainIak** - DAM2

---

## 📄 Licencia

Este proyecto es de uso educativo - DAM2 2025/2026
