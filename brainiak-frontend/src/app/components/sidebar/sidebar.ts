import { Component, Output, EventEmitter } from '@angular/core';
import { Router } from '@angular/router';
import { ThemeService } from '../../services/theme';
import { TrivialService } from '../../services/trivial';

@Component({
  selector: 'app-sidebar',
  imports: [],
  templateUrl: './sidebar.html',
  styleUrl: './sidebar.scss'
})
export class Sidebar {
  @Output() openProfile = new EventEmitter<void>();

  constructor(
    private router: Router,
    private themeService: ThemeService,
    private trivialService: TrivialService
  ) {}

  goHome(): void {
    this.router.navigate(['/']);
  }

  startGame(): void {
    this.router.navigate(['/game'], { queryParams: { mode: 'random' } });
  }

  showRanking(): void {
    this.router.navigate(['/ranking']);
  }

  toggleTheme(): void {
    this.themeService.toggleTheme();
  }
}
