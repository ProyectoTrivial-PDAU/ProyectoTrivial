

import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
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
  questionCount = 5;
  shakeCard = false;

  // Audio
  private audioCorrect: HTMLAudioElement | null = null;
  private audioWrong: HTMLAudioElement | null = null;
  private audioFinish: HTMLAudioElement | null = null;

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private http: HttpClient,
    private trivialService: TrivialService,
    private userService: UserService,
    private toastService: ToastService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.initAudio();
    this.route.queryParams.subscribe(params => {
      this.gameMode = params['mode'] || 'random';
      this.category = params['category'] || '';
      this.questionCount = parseInt(params['count']) || 5;
      this.loadQuestions();
    });
  }

  private initAudio(): void {
    // Create audio using Web Audio API oscillator tones (no external files needed)
    this.audioCorrect = this.createToneAudio(880, 0.15, 'sine');
    this.audioWrong = this.createToneAudio(220, 0.25, 'sawtooth');
    this.audioFinish = this.createToneAudio(660, 0.3, 'sine');
  }

  private createToneAudio(freq: number, duration: number, type: OscillatorType): HTMLAudioElement | null {
    // We'll play tones directly using AudioContext instead
    return null; // placeholder — actual playing is done in playSound()
  }

  private playSound(type: 'correct' | 'wrong' | 'finish'): void {
    try {
      const ctx = new (window.AudioContext || (window as any).webkitAudioContext)();
      const osc = ctx.createOscillator();
      const gain = ctx.createGain();
      osc.connect(gain);
      gain.connect(ctx.destination);
      
      if (type === 'correct') {
        osc.type = 'sine';
        osc.frequency.setValueAtTime(523, ctx.currentTime);     // C5
        osc.frequency.setValueAtTime(659, ctx.currentTime + 0.1); // E5
        osc.frequency.setValueAtTime(784, ctx.currentTime + 0.2); // G5
        gain.gain.setValueAtTime(0.15, ctx.currentTime);
        gain.gain.exponentialRampToValueAtTime(0.01, ctx.currentTime + 0.35);
        osc.start(ctx.currentTime);
        osc.stop(ctx.currentTime + 0.35);
      } else if (type === 'wrong') {
        osc.type = 'sawtooth';
        osc.frequency.setValueAtTime(300, ctx.currentTime);
        osc.frequency.exponentialRampToValueAtTime(150, ctx.currentTime + 0.3);
        gain.gain.setValueAtTime(0.1, ctx.currentTime);
        gain.gain.exponentialRampToValueAtTime(0.01, ctx.currentTime + 0.3);
        osc.start(ctx.currentTime);
        osc.stop(ctx.currentTime + 0.3);
      } else if (type === 'finish') {
        // Victory fanfare
        osc.type = 'sine';
        osc.frequency.setValueAtTime(523, ctx.currentTime);
        osc.frequency.setValueAtTime(659, ctx.currentTime + 0.15);
        osc.frequency.setValueAtTime(784, ctx.currentTime + 0.3);
        osc.frequency.setValueAtTime(1047, ctx.currentTime + 0.45);
        gain.gain.setValueAtTime(0.15, ctx.currentTime);
        gain.gain.exponentialRampToValueAtTime(0.01, ctx.currentTime + 0.6);
        osc.start(ctx.currentTime);
        osc.stop(ctx.currentTime + 0.6);
      }
    } catch (e) {
      // Audio not supported, silently fail
    }
  }

  loadQuestions(): void {
    const categoria = this.gameMode === 'category' ? this.category : undefined;
    this.trivialService.getPreguntas(this.questionCount, categoria).subscribe({
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

    // Calcular puntuación y reproducir sonido
    if (option === this.currentQuestion?.respuesta_correcta) {
      this.score++;
      this.playSound('correct');
    } else {
      this.playSound('wrong');
      this.shakeCard = true;
      setTimeout(() => this.shakeCard = false, 500);
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
    this.playSound('finish');
    const user = this.userService.getCurrentUser();
    const playerName = user?.nombre_usuario || user?.email || 'Invitado';
    const entry: RankingEntry = {
      name: playerName,
      score: this.score,
      total: this.questions.length,
      date: new Date().toISOString(),
      category: this.category || 'Aleatorio'
    };
    // Guardar ranking local
    this.userService.saveRanking(entry);
    
    // Enviar al ranking global usando HttpClient (a través del proxy)
    this.http.post(`${environment.apiUrl}/api/trivial/ranking-global`, {
      jugador: playerName,
      puntuacion: entry.score,
      totalPreguntas: entry.total,
      categoria: entry.category
    }).subscribe({
      next: () => console.log('Ranking global guardado correctamente'),
      error: (err) => console.error('Error guardando ranking global:', err)
    });

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
