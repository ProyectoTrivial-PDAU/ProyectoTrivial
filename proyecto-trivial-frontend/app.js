let preguntas = [];
let indiceActual = 0;
let puntuacion = 0;

async function mostrarPregunta() {
  document.getElementById("pantalla-inicio").style.display = "none";
  document.getElementById("pantalla-pregunta").style.display = "block";

  try {
    const res = await fetch("http://localhost:8080/api/trivial/preguntas?cantidad=5");

    console.log("Respuesta del servidor:", res);

    if (!res.ok) throw new Error(`Error HTTP ${res.status}`);

    preguntas = await res.json();
    console.log("Preguntas cargadas:", preguntas);

    if (!preguntas.length) {
      alert("No se recibieron preguntas del servidor.");
      return;
    }

    indiceActual = 0;
    puntuacion = 0;
    cargarPregunta();
  } catch (error) {
    console.error("Error al cargar preguntas:", error);
    alert("No se pudieron cargar las preguntas.");
  }



  /*
  try {
    const res = await fetch("http://localhost:8080/api/trivial/preguntas?cantidad=5");
    preguntas = await res.json(); // ✅ aquí está el arreglo directamente
    indiceActual = 0;
    puntuacion = 0;
    cargarPregunta();
  } catch (error) {
    console.error("Error al cargar preguntas:", error);
    alert("No se pudieron cargar las preguntas.");
  }
    */
}


function cargarPregunta() {
  const pregunta = preguntas[indiceActual];
  document.getElementById("numero-pregunta").textContent = indiceActual + 1;
  document.getElementById("puntuacion").textContent = puntuacion;
  document.querySelector(".question").textContent = pregunta.pregunta;

  const opcionesContainer = document.querySelector(".options");
  opcionesContainer.innerHTML = "";

  pregunta.opciones.forEach((opcion, index) => {
    const btn = document.createElement("button");
    btn.className = "option";
    btn.textContent = `${String.fromCharCode(65 + index)}. ${opcion}`;
    btn.onclick = () => verificarRespuesta(opcion, pregunta.respuesta_correcta);
    opcionesContainer.appendChild(btn);
  });
}

function verificarRespuesta(seleccionada, correcta) {
  if (seleccionada === correcta) {
    puntuacion += 1;
  }

  indiceActual += 1;
  if (indiceActual < preguntas.length) {
    cargarPregunta();
  } else {
    mostrarResultadoFinal();
  }
}

function mostrarResultadoFinal() {
  const pantalla = document.getElementById("pantalla-pregunta");
  pantalla.innerHTML = `
    <h2>¡Ronda completada!</h2>
    <p>Tu puntuación final es: ${puntuacion} de ${preguntas.length}</p>
    <button class="btn btn-back" onclick="location.reload()">Volver al inicio</button>
  `;
}


//MODO SELECCIÓN DE CATEGORÍAS
async function mostrarPantallaCategorias() {
  document.getElementById("pantalla-inicio").style.display = "none";
  document.getElementById("pantalla-categorias").style.display = "block";

  try {
    const res = await fetch("http://localhost:8080/api/trivial/categorias");
    if (!res.ok) throw new Error(`Error HTTP ${res.status}`);
    const categorias = await res.json();

    const lista = document.getElementById("lista-categorias");
    lista.innerHTML = "";

    categorias.forEach(cat => {
      const btn = document.createElement("button");
      btn.className = "btn btn-primary";
      btn.textContent = cat;
      btn.onclick = () => seleccionarCategoria(cat);
      lista.appendChild(btn);
    });

  } catch (error) {
    console.error("Error al cargar categorías:", error);
    alert("No se pudieron cargar las categorías.");
  }
}

async function seleccionarCategoria(categoria) {
  document.getElementById("pantalla-categorias").style.display = "none";
  document.getElementById("pantalla-pregunta").style.display = "block";

  try {
    const res = await fetch(`http://localhost:8080/api/trivial/preguntas?categoria=${encodeURIComponent(categoria)}&cantidad=5`);
    if (!res.ok) throw new Error(`Error HTTP ${res.status}`);
    preguntas = await res.json();

    indiceActual = 0;
    puntuacion = 0;
    cargarPregunta();
  } catch (error) {
    console.error("Error al cargar preguntas:", error);
    alert("No se pudieron cargar las preguntas de esta categoría.");
  }
}

