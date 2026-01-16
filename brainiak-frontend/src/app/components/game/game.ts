import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { CommonModule } from '@angular/common';
import { TrivialService } from '../../services/trivial';
import { UserService } from '../../services/user';
import { ToastService } from '../../services/toast';
import { Pregunta } from '../../models/pregunta';
import { RankingEntry } from '../../models/user';

@Component({
  selector: 'app-game',
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
    private toastService: ToastService
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
    this.trivialService.getPreguntas(5, categoria).subscribe({
      next: (preguntas) => {
        this.questions = preguntas;
        this.currentQuestionIndex = 0;
        this.score = 0;
        this.answered = false;
        this.selectedAnswer = null;
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
    if (this.answered) return;
    
    this.selectedAnswer = option;
    this.answered = true;

    if (option === this.currentQuestion?.respuesta_correcta) {
      this.score++;
    }

    setTimeout(() => this.nextQuestion(), 1500);
  }

  nextQuestion(): void {
    if (this.currentQuestionIndex < this.questions.length - 1) {
      this.currentQuestionIndex++;
      this.answered = false;
      this.selectedAnswer = null;
    } else {
      this.finishGame();
    }
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
    this.userService.saveRanking(entry);
    
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
}
