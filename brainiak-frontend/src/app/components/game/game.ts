

import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { CommonModule } from '@angular/common';
import { TrivialService } from '../../services/trivial';
import { UserService } from '../../services/user';
import { ToastService } from '../../services/toast';
import { Pregunta } from '../../models/pregunta';
import { RankingEntry } from '../../models/user';
import { ChangeDetectorRef } from '@angular/core';
import { environment } from '../../../environments/environment';


@Component({
  selector: 'app-game',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './game.html',
  styleUrl: './game.scss'
})


export class Game implements OnInit {
  questions: Pregunta[] = [];
  currentQuestionIndex = 0;
  score = 0;
  selectedAnswer: string | null = null;
  answered = false;
  gameMode = 'random';
  category = '';

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private trivialService: TrivialService,
    private userService: UserService,
    private toastService: ToastService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      this.gameMode = params['mode'] || 'random';
      this.category = params['category'] || '';
      this.loadQuestions();
    });
  }

  loadQuestions(): void {
    const categoria = this.gameMode === 'category' ? this.category : undefined;
    //this.trivialService.getPreguntas().subscribe({
    this.trivialService.getPreguntas(5, categoria).subscribe({
      next: (preguntas) => {
        this.questions = preguntas;
        this.currentQuestionIndex = 0;
        this.score = 0;
        this.answered = false;
        this.selectedAnswer = null;
        this.cdr.detectChanges();
      },
      error: () => {
        this.toastService.show('Error al cargar las preguntas. Asegúrate de que el servidor esté ejecutándose.', 'error');
        this.router.navigate(['/']);
      }
    });
  }

  get currentQuestion(): Pregunta | null {
    return this.questions[this.currentQuestionIndex] || null;
  }

  selectAnswer(option: string): void {
    // Evitar seleccionar si ya se respondió
    if (this.answered) return;
    
    this.selectedAnswer = option;
    this.answered = true;

    // Calcular puntuación
    if (option === this.currentQuestion?.respuesta_correcta) {
      this.score++;
    }

    // Forzar detección de cambios para mostrar colores
    this.cdr.detectChanges();

    // Esperar 1.5 segundos antes de pasar a la siguiente pregunta
    setTimeout(() => this.nextQuestion(), 1500);
  }

  nextQuestion(): void {
    this.currentQuestionIndex++;

    // Si no hay más preguntas → ir a resultados
    if (this.currentQuestionIndex >= this.questions.length) {
      this.finishGame();
      return;
    }

    // Resetear estado de la pregunta ANTES de mostrar la nueva
    this.resetQuestionState();
    
    // Forzar detección de cambios
    this.cdr.detectChanges();
  }

  resetQuestionState(): void {
    this.selectedAnswer = null;
    this.answered = false;
  }

  
  finishGame(): void {
    const user = this.userService.getCurrentUser();
    const entry: RankingEntry = {
      name: user?.name || user?.nickname || 'Invitado',
      score: this.score,
      total: this.questions.length,
      date: new Date().toISOString(),
      category: this.category || 'Aleatorio'
    };
    // Guardar ranking local
    this.userService.saveRanking(entry);
    
    // Enviar al ranking global
    fetch(`${environment.apiUrl}/api/trivial/ranking-global`, {
      
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        jugador: entry.name,
        puntuacion: entry.score,
        totalPreguntas: entry.total,
        categoria: entry.category
      })
    }).catch(err => console.error('Error guardando ranking global:', err));

    // Navegar a la pantalla de resultados
    this.router.navigate(['/results'], { 
      queryParams: { 
        score: this.score, 
        total: this.questions.length,
        mode: this.gameMode,
        category: this.category
      } 
    });
  }

  isCorrect(option: string): boolean {
    return this.answered && option === this.currentQuestion?.respuesta_correcta;
  }

  isIncorrect(option: string): boolean {
    return this.answered && option === this.selectedAnswer && option !== this.currentQuestion?.respuesta_correcta;
  }

  goHome(): void {
    this.router.navigate(['/']);
  }

  trackOption(index: number, option: string): string {
    // Incluir el índice de la pregunta para forzar re-renderizado
    return `${this.currentQuestionIndex}-${option}`;
  }


  
}
