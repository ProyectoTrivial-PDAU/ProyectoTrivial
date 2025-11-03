const API_URL = 'http://localhost:8080/api/trivial';
let currentGameMode = ''; // 'random' o 'category'
let selectedCategory = '';
let questions = [];
let currentQuestionIndex = 0;
let score = 0;

const categoryIcons = {
    'Matemáticas': '🔢',
    'Geografía': '🌍',
    'Historia': '📜',
    'Ciencia': '🔬',
    'Arte': '🎨'
};

function toggleTheme() {
    document.body.classList.toggle('dark');
    document.body.classList.toggle('light');
    
}

function showHome() {
    toggleBackButton(false); // Ocultar botón "Volver" en la pantalla de inicio
    document.getElementById('homeScreen').style.display = 'block';
    document.getElementById('categoryScreen').classList.remove('active');
    document.getElementById('gameScreen').classList.remove('active');
    document.getElementById('resultsScreen').classList.remove('active');
}

// Modo Clásico: Preguntas aleatorias de todas las categorías
async function startRandomGame() {
    currentGameMode = 'random';
    selectedCategory = '';
    currentQuestionIndex = 0;
    score = 0;
    
    try {
        // Sin parámetro de categoría = preguntas aleatorias
        const response = await fetch(`${API_URL}/preguntas?cantidad=5`);
        questions = await response.json();
        
        document.getElementById('homeScreen').style.display = 'none';
        document.getElementById('gameScreen').classList.add('active');
        
        loadQuestion();
    } catch (error) {
        console.error('Error cargando preguntas:', error);
        alert('Error al cargar las preguntas. Asegúrate de que el servidor esté ejecutándose en http://localhost:8080');
    }
}

// Modo Por Categoría: Muestra selector de categorías
async function showCategorySelection() {
    currentGameMode = 'category';
    toggleBackButton(true); // Mostrar botón "Volver" en la pantalla de inicio
    
    try {
        const response = await fetch(`${API_URL}/categorias`);
        const categories = await response.json();
        
        const categoriesGrid = document.getElementById('categoriesGrid');
        categoriesGrid.innerHTML = '';
        
        categories.forEach(category => {
            const card = document.createElement('div');
            card.className = 'category-card';
            card.onclick = () => startCategoryGame(category);
            
            const icon = document.createElement('div');
            icon.className = 'category-icon';
            icon.textContent = categoryIcons[category] || '📚';
            
            const name = document.createElement('div');
            name.textContent = category;
            
            card.appendChild(icon);
            card.appendChild(name);
            categoriesGrid.appendChild(card);
        });
        
        document.getElementById('homeScreen').style.display = 'none';
        document.getElementById('categoryScreen').classList.add('active');
    } catch (error) {
        console.error('Error cargando categorías:', error);
        alert('Error al cargar las categorías. Asegúrate de que el servidor esté ejecutándose en http://localhost:8080');
    }
}


// Controlar visibilidad del botón "Volver" en la pantalla de inicio
function toggleBackButton(show) {
  const btn = document.getElementById('backButton');
  btn.style.display = show ? 'inline-block' : 'none';
}


// Inicia juego con categoría específica
async function startCategoryGame(category) {
    selectedCategory = category;
    currentQuestionIndex = 0;
    score = 0;
    
    try {
        // Con parámetro de categoría = solo preguntas de esa categoría
        const response = await fetch(`${API_URL}/preguntas?categoria=${encodeURIComponent(category)}&cantidad=5`);
        questions = await response.json();
        
        document.getElementById('categoryScreen').classList.remove('active');
        document.getElementById('gameScreen').classList.add('active');
        
        loadQuestion();
    } catch (error) {
        console.error('Error cargando preguntas:', error);
        alert('Error al cargar las preguntas. Asegúrate de que el servidor esté ejecutándose en http://localhost:8080');
    }
}

function loadQuestion() {
    if (currentQuestionIndex >= questions.length) {
        showResults();
        return;
    }

    const question = questions[currentQuestionIndex];
    
    document.getElementById('currentQuestion').textContent = currentQuestionIndex + 1;
    document.getElementById('totalQuestions').textContent = questions.length;
    document.getElementById('score').textContent = score;
    document.getElementById('categoryDisplay').textContent = question.categoria;
    document.getElementById('questionText').textContent = question.pregunta;
    
    const optionsGrid = document.getElementById('optionsGrid');
    optionsGrid.innerHTML = '';
    
    question.opciones.forEach(option => {
        const btn = document.createElement('button');
        btn.className = 'option-btn';
        btn.textContent = option;
        btn.onclick = () => checkAnswer(option, question.respuesta_correcta);
        optionsGrid.appendChild(btn);
    });
}

function checkAnswer(selected, correct) {
    const buttons = document.querySelectorAll('.option-btn');
    buttons.forEach(btn => {
        btn.disabled = true;
        if (btn.textContent === correct) {
            btn.classList.add('correct');
        }
        if (btn.textContent === selected && selected !== correct) {
            btn.classList.add('incorrect');
        }
    });

    if (selected === correct) {
        score++;
        document.getElementById('score').textContent = score;
    }

    setTimeout(() => {
        nextQuestion();
    }, 1500);
}

function nextQuestion() {
    currentQuestionIndex++;
    loadQuestion();
}

function showResults() {
    document.getElementById('gameScreen').classList.remove('active');
    document.getElementById('resultsScreen').classList.add('active');
    document.getElementById('finalScore').textContent = score;
    document.getElementById('finalTotal').textContent = questions.length;
}

function restartGame() {
    if (currentGameMode === 'random') {
        startRandomGame();
    } else {
        showCategorySelection();
    }
}

