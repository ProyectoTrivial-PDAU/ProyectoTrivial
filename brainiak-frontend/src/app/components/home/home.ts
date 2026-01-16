import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-home',
  imports: [],
  templateUrl: './home.html',
  styleUrl: './home.scss'
})
export class Home {
  constructor(private router: Router) {}

  startRandomGame(): void {
    this.router.navigate(['/game'], { queryParams: { mode: 'random' } });
  }

  showCategorySelection(): void {
    this.router.navigate(['/categories']);
  }

  showRanking(): void {
    this.router.navigate(['/ranking']);
  }

  showUserManagement(): void {
    this.router.navigate(['/users']);
  }
}
