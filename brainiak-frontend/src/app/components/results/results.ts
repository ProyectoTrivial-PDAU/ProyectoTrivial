import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-results',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './results.html',
  styleUrl: './results.scss'
})
export class Results implements OnInit {
  score = 0;
  total = 0;
  gameMode = 'random';
  category = '';
  questionCount = 5;

  // Confetti
  confettiPieces: { left: number; delay: number; color: string }[] = [];
  private confettiColors = ['#6366f1', '#a855f7', '#ec4899', '#f59e0b', '#10b981', '#3b82f6'];

  // Animated ring
  circumference = 2 * Math.PI * 52; // r=52
  dashOffset = this.circumference;

  constructor(
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      this.score = +params['score'] || 0;
      this.total = +params['total'] || 0;
      this.gameMode = params['mode'] || 'random';
      this.category = params['category'] || '';
      this.questionCount = +params['count'] || 5;

      // Generate confetti pieces
      this.confettiPieces = Array.from({ length: 30 }, () => ({
        left: Math.random() * 100,
        delay: Math.random() * 2,
        color: this.confettiColors[Math.floor(Math.random() * this.confettiColors.length)]
      }));

      // Animate score ring after a short delay
      setTimeout(() => {
        this.dashOffset = this.circumference * (1 - this.percentage / 100);
      }, 300);
    });
  }

  get percentage(): number {
    if (this.total === 0) return 0;
    return Math.round((this.score / this.total) * 100);
  }

  get scoreColor(): string {
    if (this.percentage >= 80) return '#10b981';
    if (this.percentage >= 60) return '#f59e0b';
    if (this.percentage >= 40) return '#f97316';
    return '#ef4444';
  }

  get resultEmoji(): string {
    if (this.percentage >= 90) return '🏆';
    if (this.percentage >= 70) return '🎉';
    if (this.percentage >= 50) return '😊';
    if (this.percentage >= 30) return '🤔';
    return '💪';
  }

  get resultTitle(): string {
    if (this.percentage >= 90) return '¡Increíble!';
    if (this.percentage >= 70) return '¡Muy bien!';
    if (this.percentage >= 50) return '¡Buen intento!';
    if (this.percentage >= 30) return 'Puedes mejorar';
    return '¡Sigue intentando!';
  }

  get resultMessage(): string {
    if (this.percentage >= 90) return 'Eres un verdadero experto. ¡Impresionante!';
    if (this.percentage >= 70) return 'Gran resultado. ¡Casi perfecto!';
    if (this.percentage >= 50) return 'Vas por buen camino. ¡A seguir aprendiendo!';
    if (this.percentage >= 30) return 'No te rindas, la práctica hace al maestro.';
    return 'Cada intento es una oportunidad para aprender.';
  }

  restartGame(): void {
    if (this.gameMode === 'category') {
      this.router.navigate(['/game'], { 
        queryParams: { mode: 'category', category: this.category, count: this.questionCount } 
      });
    } else {
      this.router.navigate(['/game'], { queryParams: { mode: 'random', count: this.questionCount } });
    }
  }

  goHome(): void {
    this.router.navigate(['/']);
  }
}
