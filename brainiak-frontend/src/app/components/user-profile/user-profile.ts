import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { UserService } from '../../services/user';
import { User, RankingEntry } from '../../models/user';

@Component({
  selector: 'app-user-profile',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './user-profile.html',
  styleUrl: './user-profile.scss'
})
export class UserProfile implements OnInit {
  user: User | null = null;
  recentGames: RankingEntry[] = [];
  totalGames = 0;
  bestScore = '';
  avgScore = 0;
  streak = 0;
  favoriteCategory = '';
  totalXP = 0;
  level = 1;
  xpProgress = 0;
  xpToNext = 100;

  readonly levelTitles = ['Novato', 'Aprendiz', 'Curioso', 'Estudioso', 'Sabio', 'Experto', 'Maestro', 'Leyenda', 'Genio', 'Omnisciente'];

  constructor(
    private userService: UserService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.user = this.userService.getCurrentUser();
    if (!this.user) {
      this.router.navigate(['/']);
      return;
    }
    this.computeStats();
  }

  get levelTitle(): string {
    return this.levelTitles[Math.min(this.level - 1, this.levelTitles.length - 1)];
  }

  private computeStats(): void {
    const allGames = this.userService.getRankings();
    // Filter games for this user
    const userName = this.user?.nombre_usuario || this.user?.email || '';
    const myGames = allGames.filter(g => g.name === userName);

    this.totalGames = myGames.length;
    this.recentGames = myGames.slice(-5).reverse();

    if (myGames.length > 0) {
      // Best score
      const bestGame = myGames.reduce((best, g) => {
        const pct = g.total > 0 ? (g.score / g.total) : 0;
        const bestPct = best.total > 0 ? (best.score / best.total) : 0;
        return pct > bestPct ? g : best;
      });
      this.bestScore = `${bestGame.score}/${bestGame.total}`;

      // Average
      const totalPct = myGames.reduce((sum, g) => sum + (g.total > 0 ? (g.score / g.total) * 100 : 0), 0);
      this.avgScore = Math.round(totalPct / myGames.length);

      // Streak (consecutive games with >=50%)
      let streak = 0;
      for (let i = myGames.length - 1; i >= 0; i--) {
        if (myGames[i].total > 0 && (myGames[i].score / myGames[i].total) >= 0.5) {
          streak++;
        } else break;
      }
      this.streak = streak;

      // Favorite category
      const catCount: Record<string, number> = {};
      myGames.forEach(g => { catCount[g.category] = (catCount[g.category] || 0) + 1; });
      this.favoriteCategory = Object.entries(catCount).sort((a, b) => b[1] - a[1])[0]?.[0] || '';

      // XP: 10 per correct answer
      this.totalXP = myGames.reduce((sum, g) => sum + g.score * 10, 0);
      this.level = Math.floor(this.totalXP / 100) + 1;
      const xpInLevel = this.totalXP % 100;
      this.xpProgress = xpInLevel;
      this.xpToNext = 100 - xpInLevel;
    } else {
      this.bestScore = '-';
    }
  }

  getPercent(game: RankingEntry): number {
    return game.total > 0 ? Math.round((game.score / game.total) * 100) : 0;
  }

  formatDate(dateStr: string): string {
    try {
      const d = new Date(dateStr);
      return d.toLocaleDateString('es-ES', { day: 'numeric', month: 'short' });
    } catch {
      return dateStr;
    }
  }

  goPlay(): void {
    this.router.navigate(['/']);
  }

  logout(): void {
    this.userService.logout();
    this.router.navigate(['/']);
  }
}
