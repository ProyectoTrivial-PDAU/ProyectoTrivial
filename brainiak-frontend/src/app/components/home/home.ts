import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { Dialog } from '@angular/cdk/dialog';
import { DialogModule } from '@angular/cdk/dialog';
import { UserService } from '../../services/user';
import { RankingEntry } from '../../models/user';
import { QuestionCountDialog } from '../question-count-dialog/question-count-dialog';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, DialogModule],
  templateUrl: './home.html',
  styleUrl: './home.scss'
})
export class Home implements OnInit {
  // Stats
  totalGames = 0;
  avgScore = 0;
  streak = 0;
  level = 1;
  levelTitle = 'Novato';
  lastGame: RankingEntry | null = null;
  lastGameEmoji = '🎮';
  dailyTip = '';

  private readonly levelTitles = ['Novato', 'Aprendiz', 'Curioso', 'Estudioso', 'Sabio', 'Experto', 'Maestro', 'Leyenda', 'Genio', 'Omnisciente'];

  private readonly tips = [
    'El cerebro humano consume un 20% de la energía total del cuerpo.',
    'La memoria a corto plazo puede retener entre 5 y 9 elementos.',
    'Dormir bien mejora la consolidación de la memoria.',
    'Aprender algo nuevo crea nuevas conexiones neuronales.',
    'El ejercicio físico mejora la función cognitiva.',
    'Leer 20 minutos al día te expone a 1.8 millones de palabras al año.',
    'El cerebro puede procesar imágenes en tan solo 13 milisegundos.',
    'Jugar a trivia regularmente mejora tu memoria a largo plazo.',
    'El océano cubre más del 70% de la superficie de la Tierra.',
    'Cleopatra vivió más cerca en el tiempo de la Pizza Hut que de las Pirámides.',
    'Los pulpos tienen tres corazones y sangre azul.',
    'La miel nunca se echa a perder; se ha encontrado miel comestible en tumbas egipcias.',
    'Venus es el único planeta que gira en sentido contrario a los demás.',
    'El ADN humano comparte un 60% de sus genes con los plátanos.',
    'Un rayo tiene cinco veces más calor que la superficie del Sol.'
  ];

  constructor(
    private router: Router,
    private userService: UserService,
    private dialog: Dialog
  ) {}

  ngOnInit(): void {
    this.computeStats();
    this.pickDailyTip();
  }

  private computeStats(): void {
    const user = this.userService.getCurrentUser();
    if (!user) return;
    const userName = user.nombre_usuario || user.email || '';
    const allGames = this.userService.getRankings();
    const myGames = allGames.filter(g => g.name === userName);

    this.totalGames = myGames.length;

    if (myGames.length > 0) {
      const totalPct = myGames.reduce((sum, g) => sum + (g.total > 0 ? (g.score / g.total) * 100 : 0), 0);
      this.avgScore = Math.round(totalPct / myGames.length);

      let streak = 0;
      for (let i = myGames.length - 1; i >= 0; i--) {
        if (myGames[i].total > 0 && (myGames[i].score / myGames[i].total) >= 0.5) streak++;
        else break;
      }
      this.streak = streak;

      const totalXP = myGames.reduce((sum, g) => sum + g.score * 10, 0);
      this.level = Math.floor(totalXP / 100) + 1;
      this.levelTitle = this.levelTitles[Math.min(this.level - 1, this.levelTitles.length - 1)];

      this.lastGame = myGames[myGames.length - 1];
      if (this.lastGame) {
        const pct = this.lastGame.total > 0 ? (this.lastGame.score / this.lastGame.total) * 100 : 0;
        this.lastGameEmoji = pct >= 80 ? '🏆' : pct >= 50 ? '😊' : '💪';
      }
    }
  }

  private pickDailyTip(): void {
    const dayOfYear = Math.floor((Date.now() - new Date(new Date().getFullYear(), 0, 0).getTime()) / 86400000);
    this.dailyTip = this.tips[dayOfYear % this.tips.length];
  }

  openCountModal(mode: 'random' | 'category'): void {
    const dialogRef = this.dialog.open<number>(QuestionCountDialog, {
      data: {
        title: '¿Cuántas preguntas?',
        subtitle: 'Elige entre 5 y 20 preguntas',
        emoji: mode === 'random' ? '🎲' : '🎯',
        initialCount: 5,
        min: 5,
        max: 20,
        playLabel: '🚀 ¡Jugar!'
      },
      panelClass: 'brainiak-dialog-panel',
      backdropClass: 'brainiak-dialog-backdrop'
    });

    dialogRef.closed.subscribe((count) => {
      if (!count) return;
      if (mode === 'random') {
        this.router.navigate(['/game'], { queryParams: { mode: 'random', count } });
      } else {
        this.router.navigate(['/categorias'], { queryParams: { count } });
      }
    });
  }

  showCategorySelection(): void {
    this.router.navigate(['/categorias']);
  }

  showRanking(): void {
    this.router.navigate(['/ranking']);
  }

  showUserManagement(): void {
    this.router.navigate(['/usuarios']);
  }

  replayLast(): void {
    if (!this.lastGame) return;
    if (this.lastGame.category && this.lastGame.category !== 'Aleatorio') {
      this.router.navigate(['/game'], { queryParams: { mode: 'category', category: this.lastGame.category, count: this.lastGame.total } });
    } else {
      this.router.navigate(['/game'], { queryParams: { mode: 'random', count: this.lastGame.total } });
    }
  }
}
