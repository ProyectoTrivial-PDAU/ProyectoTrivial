# Documento de estado: lo bueno, lo malo, que hay hecho, donde y como

Fecha: 10/04/2026
Proyecto: Brainiak (Angular + Spring Boot)

## 1. Resumen ejecutivo

Estado general: el bloque de frontend evaluado por la rubrica esta mayormente completado.

Resultado rapido por criterio:
- a) Utilidades y clases del framework: Cumplido.
- b) Uso y personalizacion de componentes predefinidos: Cumplido (basado en Angular CDK, no Angular Material visual).
- c) Modificacion de disposicion de interfaz: Cumplido.
- d) Personalizacion de estilos y temas: Cumplido.
- e) Diseno responsive/adaptativo: Cumplido.
- f) Animaciones y transiciones: Cumplido.

## 2. Lo bueno (con evidencia de donde y como)

### a) Utilidades y clases del framework
Donde:
- `brainiak-frontend/src/app/app.config.ts:1`
- `brainiak-frontend/src/app/components/category-selection/category-selection.ts:1`

Como:
- Se usa configuracion moderna de Angular con providers (`provideRouter`, `provideHttpClient`).
- Se usa inyeccion de dependencias en componentes y servicios.
- Se usan herramientas del framework para reaccionar al tamano de pantalla (`BreakpointObserver`).

### b) Componentes predefinidos personalizados
Donde:
- `brainiak-frontend/src/app/components/category-selection/category-selection.ts:5`
- `brainiak-frontend/src/app/components/category-selection/category-selection.html:15`
- `brainiak-frontend/src/app/components/question-count-dialog/question-count-dialog.ts:3`

Como:
- Se usa `@angular/cdk/dialog` para crear modal propio de seleccion de preguntas.
- Se usa `cdk-virtual-scroll-viewport` para optimizar render de categorias.
- Se combina CDK con estilos propios para personalizacion visual completa.

### c) Modificacion de la disposicion de interfaz
Donde:
- `brainiak-frontend/src/app/components/category-selection/category-selection.scss:44`
- `brainiak-frontend/src/app/components/ranking/ranking.scss:398`

Como:
- Diseno basado en `grid` y `flex` segun pantalla.
- El ranking incorpora tabla, podio y paginacion local/global.
- La seleccion de categorias reorganiza columnas dinamicamente.

### d) Personalizacion de estilos y temas
Donde:
- `brainiak-frontend/src/app/services/theme.ts:1`
- `brainiak-frontend/src/styles.scss:10`
- `brainiak-frontend/src/styles.scss:24`

Como:
- Sistema de tema dark/light persistido en `localStorage`.
- Variables CSS globales y gradientes para identidad visual.
- Clases compartidas (glass, botones, etc.) para coherencia de UI.

### e) Diseno responsive y adaptativo
Donde:
- `brainiak-frontend/src/app/components/category-selection/category-selection.ts:78`
- `brainiak-frontend/src/app/components/category-selection/category-selection.scss:99`
- `brainiak-frontend/src/app/components/ranking/ranking.scss:398`

Como:
- Breakpoints logicos en TypeScript (1, 2, 3 o 4 columnas).
- Media queries en SCSS para adaptar grid, viewport y tabla en movil.
- Ajustes de tamano y ocultacion selectiva de columnas en ranking movil.

### f) Animaciones y transiciones
Donde:
- `brainiak-frontend/src/styles.scss:44`
- `brainiak-frontend/src/app/components/category-selection/category-selection.scss:10`
- `brainiak-frontend/src/app/components/category-selection/category-selection.scss:60`

Como:
- Biblioteca propia de `@keyframes` globales (`fadeInUp`, `spin`, `bounceIn`, etc.).
- Entrada animada de tarjetas y estados de carga.
- Transiciones de hover/active consistentes en componentes interactivos.

## 3. Lo malo (riesgos, deuda tecnica y puntos mejorables)

### 3.1 Dependencia fuerte del backend para ranking global
Donde:
- `brainiak-frontend/src/app/components/ranking/ranking.ts:75`
- `ProyectoTrivial/src/main/resources/application.properties:3`

Que pasa:
- Si el backend arranca con perfil no valido para local, el endpoint global falla y la UI muestra error de conexion.

Como se mitigo:
- Se dejo perfil local por defecto en backend: `spring.profiles.active=${SPRING_PROFILES_ACTIVE:dev}`.

Riesgo residual:
- En despliegue, hay que fijar `SPRING_PROFILES_ACTIVE=render` para usar la configuracion de produccion esperada.

### 3.2 Tipado debil en ranking global
Donde:
- `brainiak-frontend/src/app/components/ranking/ranking.ts:17`
- `brainiak-frontend/src/app/components/ranking/ranking.ts:75`

Que pasa:
- Se usa `any[]` para el ranking global y mapeos defensivos por nombres de campo alternativos.

Impacto:
- Menor seguridad de tipos y mas posibilidad de errores silenciosos si cambia el backend.

### 3.3 Falta de evidencia de tests de interfaz para estos criterios
Donde:
- No hay evidencia directa en los archivos revisados para pruebas e2e o visuales de responsive/animaciones.

Impacto:
- El comportamiento existe, pero la validacion automatizada para la rubrica no queda respaldada por pruebas formales.

### 3.4 Desfase de documentacion vs stack real
Donde:
- `README.md` (referencia a Angular 19)
- `brainiak-frontend/package.json:15` (Angular 21)

Impacto:
- Puede generar confusion en evaluacion o instalacion por parte de terceros.

## 4. Evidencia resumida por rubrica (lista util para entrega)

- a) Framework: providers, DI, router/http, CDK layout.
- b) Componentes predefinidos: Dialog, A11y, Virtual Scroll (CDK) personalizados.
- c) Disposicion UI: grid/flex/paginacion y composicion de pantallas.
- d) Estilos/temas: tema oscuro-claro, variables y tokens de estilo.
- e) Responsive: breakpoints en TS + media queries en SCSS.
- f) Animaciones: keyframes globales + transiciones y animaciones por componente.

## 5. Recomendaciones concretas para cerrar la entrega al 100%

1. Crear interfaz tipada para ranking global en frontend y eliminar `any[]`.
2. Anadir 1-2 tests de comportamiento responsive (al menos para `category-selection` y `ranking`).
3. Actualizar `README.md` para que version de Angular coincida con `package.json`.
4. Dejar documentado en despliegue la variable `SPRING_PROFILES_ACTIVE=render`.

## 6. Conclusiones

- La parte principal de la rubrica esta implementada y demostrable en codigo.
- Lo mas relevante pendiente no es de funcionalidad base, sino de robustez (tipado, testing y documentacion).
- Con los ajustes recomendados, la entrega queda mas defendible en revision tecnica y academica.
