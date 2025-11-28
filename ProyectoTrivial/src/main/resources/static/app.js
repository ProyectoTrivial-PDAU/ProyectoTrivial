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


function hideAllScreens() {
    document.getElementById('homeScreen').style.display = 'none';
    document.getElementById('categoryScreen').classList.remove('active');
    document.getElementById('gameScreen').classList.remove('active');
    document.getElementById('resultsScreen').classList.remove('active');
    document.getElementById('rankingScreen').style.display = 'none';
    document.getElementById('usersScreen').style.display = 'none';
}

function resetGameUI() {
document.getElementById('optionsGrid').innerHTML = '';
document.getElementById('questionText').textContent = '';
document.getElementById('categoryDisplay').textContent = '';
document.getElementById('score').textContent = '0';
document.getElementById('finalScore').textContent = '0';
document.getElementById('finalTotal').textContent = '0';
}

function toggleTheme() {
    const isDark = document.body.classList.contains('dark');
    if (isDark) {
        document.body.classList.remove('dark');
        document.body.classList.add('light');
        localStorage.setItem('theme', 'light');
    } else {
        document.body.classList.remove('light');
        document.body.classList.add('dark');
        localStorage.setItem('theme', 'dark');
    }
    updateThemeButton();
}

// --- Gestión de usuario local (perfil simple) ---
function getStoredUser() {
    try {
        const raw = localStorage.getItem('user_profile');
        return raw ? JSON.parse(raw) : null;
    } catch (e) {
        return null;
    }
}

function saveStoredUser(user) {
    localStorage.setItem('user_profile', JSON.stringify(user));
}

function openProfileModal() {
    const modal = document.getElementById('profileModal');
    const user = getStoredUser() || { name: '', nickname: '' };
    document.getElementById('inputName').value = user.name || '';
    document.getElementById('inputNickname').value = user.nickname || '';
    document.getElementById('profileError').style.display = 'none';
        // show modal and setup focus trap
        modal.style.display = 'flex';
        modal.setAttribute('aria-hidden', 'false');
        // save previously focused element to restore later
        modal._previouslyFocused = document.activeElement;
        const firstInput = document.getElementById('inputName');
        firstInput.focus();
        // listen keys for escape and tab handling
        document.addEventListener('keydown', modal._keyHandler = (e) => handleModalKeydown(e, modal));
}

function closeProfileModal() {
    const modal = document.getElementById('profileModal');
    modal.style.display = 'none';
    modal.setAttribute('aria-hidden', 'true');
        // remove modal key handler and restore focus
        if (modal._keyHandler) {
            document.removeEventListener('keydown', modal._keyHandler);
            modal._keyHandler = null;
        }
        try {
            if (modal._previouslyFocused) modal._previouslyFocused.focus();
        } catch (e) { }
}

async function saveProfileFromModal() {
    const name = document.getElementById('inputName').value.trim();
    const nickname = document.getElementById('inputNickname').value.trim();
    const errorEl = document.getElementById('profileError');
    if (!name) {
        errorEl.textContent = 'El nombre no puede estar vacío.';
        errorEl.style.display = 'block';
        showToast('El nombre no puede estar vacío.', 'error');
        return;
    }
    const user = { name, nickname };
        // Try to save to backend first, fallback to localStorage
        const apiBase = API_URL.replace('/trivial', '');
        let savedOnServer = false;
        try {
            const resp = await fetch(`${apiBase}/usuarios`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(user)
            });
            if (resp.ok) {
                savedOnServer = true;
            }
        } catch (e) {
            // ignore network errors, we'll fallback
        }

        saveStoredUser(user);
        applyUserToUI(user);
        closeProfileModal();

        if (savedOnServer) {
            showToast('Perfil guardado y sincronizado con servidor.', 'success');
        } else {
            showToast('Perfil guardado localmente (sincronización pendiente).', 'info');
        }
}

    function handleModalKeydown(e, modal) {
        // Close on Escape
        if (e.key === 'Escape') {
            closeProfileModal();
            return;
        }
        // Focus trap: keep focus within modal when tabbing
        if (e.key === 'Tab') {
            const focusable = modal.querySelectorAll('a[href], button:not([disabled]), textarea, input, select, [tabindex]:not([tabindex="-1"])');
            if (!focusable || focusable.length === 0) return;
            const first = focusable[0];
            const last = focusable[focusable.length - 1];
            if (e.shiftKey) {
                if (document.activeElement === first) {
                    e.preventDefault();
                    last.focus();
                }
            } else {
                if (document.activeElement === last) {
                    e.preventDefault();
                    first.focus();
                }
            }
        }
    }

function applyUserToUI(user) {
    const nameEl = document.getElementById('profileName');
    const avatarEl = document.getElementById('profileAvatar');
    if (!user) {
        nameEl.textContent = 'Invitado';
        avatarEl.textContent = '👤';
        return;
    }
    nameEl.textContent = user.name || (user.nickname || 'Invitado');
    // usar initiales si no hay avatar
    if (user.name) {
        const initials = user.name.split(' ').map(s => s[0]).slice(0,2).join('').toUpperCase();
        avatarEl.textContent = initials || '👤';
    } else {
        avatarEl.textContent = user.nickname ? user.nickname[0].toUpperCase() : '👤';
    }
}

function updateThemeButton() {
    const themeBtn = document.getElementById('themeBtn');
    if (!themeBtn) return;
    const isDark = document.body.classList.contains('dark');
    themeBtn.textContent = isDark ? '🌙' : '☀️';
}

function initApp() {
    // Aplicar tema guardado
    const savedTheme = localStorage.getItem('theme');
    if (savedTheme === 'light') {
        document.body.classList.remove('dark');
        document.body.classList.add('light');
    } else {
        document.body.classList.remove('light');
        document.body.classList.add('dark');
    }
    updateThemeButton();

    // Cargar usuario y aplicar en UI
    const user = getStoredUser();
    applyUserToUI(user);

    // Listeners para modal y botones
    const saveBtn = document.getElementById('saveProfileBtn');
    if (saveBtn) saveBtn.addEventListener('click', saveProfileFromModal);
    const cancelBtn = document.getElementById('cancelProfileBtn');
    if (cancelBtn) cancelBtn.addEventListener('click', closeProfileModal);
    // cerrar modal con click fuera
    const modal = document.getElementById('profileModal');
    if (modal) modal.addEventListener('click', (e) => { if (e.target === modal) closeProfileModal(); });
}

window.addEventListener('DOMContentLoaded', initApp);

function showHome() {
    hideAllScreens();
    toggleBackButton(false); // Ocultar botón "Volver" en la pantalla de inicio
    document.getElementById('homeScreen').style.display = 'block';
    document.getElementById('categoryScreen').classList.remove('active');
    document.getElementById('gameScreen').classList.remove('active');
    document.getElementById('resultsScreen').classList.remove('active');
}

// Modo Clásico: Preguntas aleatorias de todas las categorías
async function startRandomGame() {
    hideAllScreens();
    document.getElementById('homeScreen').style.display = 'block';
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
        showToast('Error al cargar las preguntas. Asegúrate de que el servidor esté ejecutándose en http://localhost:8080', 'error');
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
        showToast('Error al cargar las categorías. Asegúrate de que el servidor esté ejecutándose en http://localhost:8080', 'error');
    }
}


// Controlar visibilidad del botón "Volver" en la pantalla de inicio
function toggleBackButton(show) {
  const btn = document.getElementById('backButton');
  btn.style.display = show ? 'inline-block' : 'none';
}


// Inicia juego con categoría específica
async function startCategoryGame(category) {
    hideAllScreens();
    resetGameUI();
    selectedCategory = category;
    currentQuestionIndex = 0;
    score = 0;

    try {
        // Con parámetro de categoría = solo preguntas de esa categoría
        const response = await fetch(`${API_URL}/preguntas?categoria=${encodeURIComponent(category)}&cantidad=5`);
        questions = await response.json();

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
    // Guardar resultado en ranking local
    try {
        const user = getStoredUser();
        const entry = {
            name: (user && (user.name || user.nickname)) || 'Invitado',
            score: score,
            total: questions.length,
            date: new Date().toISOString(),
            category: selectedCategory || 'Aleatorio'
        };
        const raw = localStorage.getItem('trivial_rankings');
        const arr = raw ? JSON.parse(raw) : [];
        arr.push(entry);
        localStorage.setItem('trivial_rankings', JSON.stringify(arr));
    } catch (e) { console.warn('No se pudo guardar ranking local', e); }
}

/*function showRanking() {
    // esconder otras pantallas
    document.getElementById('homeScreen').style.display = 'none';
    document.getElementById('categoryScreen').classList.remove('active');
    document.getElementById('gameScreen').classList.remove('active');
    document.getElementById('resultsScreen').classList.remove('active');
    // mostrar ranking
    document.getElementById('rankingScreen').style.display = 'block';
    document.getElementById('rankingList').innerHTML = '';
    try {
        const raw = localStorage.getItem('trivial_rankings');
        const arr = raw ? JSON.parse(raw) : [];
        arr.sort((a,b) => b.score - a.score || new Date(b.date) - new Date(a.date));
        if (arr.length === 0) {
            document.getElementById('rankingList').innerHTML = '<p style="text-align:center;">No hay partidas guardadas aún.</p>';
            return;
        }
        const list = document.getElementById('rankingList');
        arr.forEach((r, idx) => {
            const item = document.createElement('div');
            item.className = 'ranking-item';
            item.innerHTML = `<div class="ranking-pos">${idx+1}</div><div class="ranking-info"><div class="ranking-name">${escapeHtml(r.name)}</div><div class="ranking-meta">${r.score}/${r.total} • ${new Date(r.date).toLocaleString()} • ${r.category}</div></div>`;
            list.appendChild(item);
        });
    } catch (e) { console.error(e); }
}
*/
function showRanking() {

hideAllScreens();

    document.getElementById('rankingScreen').style.display = 'block';

    const tableContainer = document.getElementById('rankingList');
    tableContainer.innerHTML = '';

    const raw = localStorage.getItem('trivial_rankings');
    const rankings = raw ? JSON.parse(raw) : [];

    if (rankings.length === 0) {
        tableContainer.innerHTML = '<p style="text-align:center;">No hay partidas guardadas aún.</p>';
        return;
    }

    rankings.sort(
        (a, b) => b.score - a.score || new Date(b.date) - new Date(a.date)
    );

    const table = document.createElement('table');
    table.className = 'ranking-table';

    table.innerHTML = `
        <tr>
            <th>#</th>
            <th>Jugador</th>
            <th>Puntuación</th>
            <th>Categoría</th>
            <th>Fecha</th>
        </tr>
    `;

    rankings.forEach((r, i) => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${i + 1}</td>
            <td>${escapeHtml(r.name)}</td>
            <td>${r.score}/${r.total}</td>
            <td>${escapeHtml(r.category)}</td>
            <td>${new Date(r.date).toLocaleString()}</td>
        `;
        table.appendChild(row);
    });

    tableContainer.appendChild(table);
}

function escapeHtml(s) { return String(s).replace(/[&<>"]+/g, c => ({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;'}[c] || c)); }

/* Toasts */
function showToast(message, type = 'info', duration = 3500) {
    try {
        const container = document.getElementById('toastContainer');
        if (!container) return;
        const toast = document.createElement('div');
        toast.className = `toast ${type}`;
        const icon = document.createElement('div');
        icon.className = 'toast-icon';
        icon.textContent = type === 'success' ? '✓' : (type === 'error' ? '⚠' : 'ℹ');
        const text = document.createElement('div');
        text.className = 'toast-text';
        text.textContent = message;
        toast.appendChild(icon);
        toast.appendChild(text);
        container.appendChild(toast);

        const remove = () => {
            toast.style.animation = 'toast-out 200ms ease-in forwards';
            setTimeout(() => { toast.remove(); }, 200);
        };

        setTimeout(remove, duration);
        // click to remove
        toast.addEventListener('click', remove);
    } catch (e) { /* silently ignore */ }
}

async function showUserManagement() {
    // esconder pantallas
    document.getElementById('homeScreen').style.display = 'none';
    document.getElementById('categoryScreen').classList.remove('active');
    document.getElementById('gameScreen').classList.remove('active');
    document.getElementById('resultsScreen').classList.remove('active');
    document.getElementById('rankingScreen').style.display = 'none';
    document.getElementById('usersScreen').style.display = 'block';
    const listEl = document.getElementById('usersList');
    listEl.innerHTML = '<p>Cargando usuarios...</p>';
    const apiBase = API_URL.replace('/trivial', '');
    try {
        const resp = await fetch(`${apiBase}/usuarios`);
        if (!resp.ok) throw new Error('Error respondiendo');
        const users = await resp.json();
        if (!users || users.length === 0) {
            listEl.innerHTML = '<p>No hay usuarios registrados en el servidor.</p>';
            return;
        }
        listEl.innerHTML = '';
        users.forEach(u => {
            const row = document.createElement('div');
            row.className = 'user-row';
            row.innerHTML = `<div class="user-name">${escapeHtml(u.name)} <span class="user-nick">${escapeHtml(u.nickname || '')}</span></div>`;
            const actions = document.createElement('div');
            actions.className = 'user-actions';
            const del = document.createElement('button');
            del.className = 'auth-btn';
            del.textContent = 'Eliminar';
            del.onclick = async () => {
                if (!confirm('Eliminar usuario ' + u.name + '?')) return;
                try {
                    const dresp = await fetch(`${apiBase}/usuarios/${u.id}`, { method: 'DELETE' });
                    if (dresp.ok) {
                        row.remove();
                        showToast('Usuario eliminado', 'success');
                    } else {
                        showToast('No se pudo eliminar en el servidor', 'error');
                    }
                } catch (e) { showToast('Error de red', 'error'); }
            };
            actions.appendChild(del);
            row.appendChild(actions);
            listEl.appendChild(row);
        });
    } catch (e) {
        listEl.innerHTML = '<p>Error cargando usuarios desde el servidor.</p>';
        showToast('Error cargando usuarios desde el servidor', 'error');
    }
}

function restartGame() {
    if (currentGameMode === 'random') {
        startRandomGame();
    } else {
        hideAllScreens();
        showCategorySelection();
    }
}

function updateRankingScreen() {
    const ranking = JSON.parse(localStorage.getItem("ranking") || "[]");

    const tableBody = document.getElementById("rankingTableBody");
    tableBody.innerHTML = "";

    ranking.forEach((entry, index) => {
        const row = document.createElement("tr");

        row.innerHTML = `
            <td>${index + 1}</td>
            <td>${entry.nombre || "Jugador"}</td>
            <td>${entry.puntuacion}</td>
            <td>${entry.partidas || 1}</td>
        `;

        tableBody.appendChild(row);
    });
}

