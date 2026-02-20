# 🎨 Brainiak Frontend - Angular

Frontend de la aplicación Brainiak desarrollado con **Angular 19**.

## 📁 Estructura del Proyecto

```
src/app/
├── components/                    # Componentes de la UI
│   ├── sidebar/                   # Barra lateral de navegación
│   │   ├── sidebar.ts             # Lógica del componente
│   │   ├── sidebar.html           # Template
│   │   └── sidebar.scss           # Estilos
│   ├── header/                    # Cabecera con logo y perfil
│   ├── home/                      # Pantalla de inicio
│   ├── game/                      # Pantalla de juego
│   ├── results/                   # Pantalla de resultados
│   ├── category-selection/        # Selector de categorías
│   ├── ranking/                   # Tabla de rankings
│   ├── users/                     # Gestión de usuarios
│   ├── profile-modal/             # Modal de edición de perfil
│   └── toast/                     # Sistema de notificaciones
│
├── services/                      # Servicios inyectables
│   ├── trivial.ts                 # Comunicación con API
│   ├── user.ts                    # Gestión de usuarios
│   ├── theme.ts                   # Tema oscuro/claro
│   └── toast.ts                   # Notificaciones
│
├── models/                        # Interfaces TypeScript
│   ├── pregunta.ts                # Modelo de pregunta
│   └── user.ts                    # Modelo de usuario
│
├── app.ts                         # Componente raíz
├── app.html                       # Template principal
├── app.scss                       # Estilos principales
├── app.routes.ts                  # Configuración de rutas
└── app.config.ts                  # Configuración de la app
```

---

## 🚀 Comandos

### Instalación
```bash
npm install
```

### Desarrollo
```bash
ng serve --port 4200
# o
npm start
```
Abre `http://localhost:4200` en el navegador.

### Build de producción
```bash
ng build --configuration production
```
Los archivos se generan en `dist/brainiak-frontend/`

### Tests
```bash
ng test
```

### Linting
```bash
ng lint
```

---

## 🧩 Componentes

### Sidebar
Barra de navegación lateral con iconos para:
- 🏠 Inicio
- 🎮 Jugar
- 🏆 Ranking
- 👤 Perfil
- 🌙 Toggle tema

### Header
Cabecera con:
- Logo de Brainiak
- Información del usuario actual
- Avatar con iniciales

### Home
Pantalla principal con:
- Mensaje de bienvenida
- Tarjetas de modos de juego (Clásico, Por Categoría)
- Accesos a Ranking y Gestión de usuarios

### Game
Pantalla de juego con:
- Contador de pregunta actual
- Puntuación en tiempo real
- Tarjeta de pregunta con opciones
- Feedback visual (verde/rojo) al responder

### Results
Pantalla de resultados con:
- Puntuación final
- Botones para jugar de nuevo o volver al inicio

### CategorySelection
Grid de categorías disponibles con iconos:
- 🔢 Matemáticas
- 🌍 Geografía
- 📜 Historia
- 🔬 Ciencia
- 🎨 Arte

### Ranking
Tabla con historial de partidas:
- Posición
- Nombre del jugador
- Puntuación
- Categoría
- Fecha

### ProfileModal
Modal para editar el perfil:
- Nombre
- Nickname
- Guardado local y sincronización con servidor

### Toast
Sistema de notificaciones tipo toast:
- ℹ️ Info (azul)
- ✓ Success (verde)
- ⚠ Error (rojo)

---

## 🔌 Servicios

### TrivialService
```typescript
// Obtener preguntas aleatorias
getPreguntas(cantidad: number): Observable<Pregunta[]>

// Obtener preguntas por categoría
getPreguntas(cantidad: number, categoria: string): Observable<Pregunta[]>

// Obtener categorías disponibles
getCategorias(): Observable<string[]>
```

### UserService
```typescript
// Usuario actual (observable)
user$: Observable<User | null>

// Guardar usuario localmente
saveUser(user: User): void

// Guardar en servidor
saveUserToServer(user: User): Observable<User>

// Rankings locales
getRankings(): RankingEntry[]
saveRanking(entry: RankingEntry): void
```

### ThemeService
```typescript
// Tema actual (observable)
theme$: Observable<'dark' | 'light'>

// Cambiar tema
toggleTheme(): void
```

### ToastService
```typescript
// Mostrar notificación
show(message: string, type: 'info' | 'success' | 'error'): void

// Eliminar notificación
remove(id: number): void
```

---

## 🛣️ Rutas

| Ruta | Componente | Descripción |
|------|------------|-------------|
| `/` | Home | Pantalla principal |
| `/game` | Game | Pantalla de juego |
| `/game?mode=category&category=Historia` | Game | Juego por categoría |
| `/results` | Results | Resultados |
| `/categories` | CategorySelection | Selector de categorías |
| `/ranking` | Ranking | Tabla de ranking |
| `/users` | Users | Gestión de usuarios |

---

## 🎨 Temas

La aplicación soporta tema **oscuro** y **claro**:

### Oscuro (por defecto)
- Fondo: gradiente azul oscuro
- Texto: blanco
- Componentes: fondos semi-transparentes

### Claro
- Fondo: gradiente azul claro
- Texto: negro
- Componentes: fondos blancos

El tema se persiste en `localStorage` y se aplica automáticamente.

---

## 📡 Conexión con Backend

El frontend se conecta al backend en `http://localhost:8080`.

Para cambiar la URL del API, edita `src/app/services/trivial.ts`:
```typescript
private apiUrl = 'http://localhost:8080/api/trivial';
```

---

## 📦 Dependencias principales

- `@angular/core` - Framework Angular
- `@angular/router` - Routing SPA
- `@angular/common` - Directivas comunes
- `rxjs` - Programación reactiva

---

## 🔧 Configuración adicional

### CORS
El backend debe permitir peticiones desde `http://localhost:4200`.

### Proxy (opcional)
Para desarrollo, puedes configurar un proxy en `proxy.conf.json`:
```json
{
  "/api": {
    "target": "http://localhost:8080",
    "secure": false
  }
}
```

Y ejecutar con:
```bash
ng serve --proxy-config proxy.conf.json
```

---

## 📝 Convenciones de código

- **Componentes**: PascalCase (`HomeComponent`)
- **Servicios**: PascalCase con sufijo (`TrivialService`)
- **Archivos**: kebab-case (`category-selection.ts`)
- **Variables**: camelCase (`currentQuestion`)
- **Constantes**: UPPER_SNAKE_CASE (`API_URL`)

---

## 🧪 Testing

Los tests se encuentran en archivos `.spec.ts` junto a cada componente.

```bash
# Ejecutar todos los tests
ng test

# Con cobertura
ng test --code-coverage
```

---

## 📄 Licencia

Proyecto educativo - DAM2 2025/2026
