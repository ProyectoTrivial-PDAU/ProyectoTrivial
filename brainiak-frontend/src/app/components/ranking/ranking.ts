import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { UserService } from '../../services/user';
import { RankingEntry } from '../../models/user';

@Component({
  selector: 'app-ranking',
  imports: [CommonModule],
  templateUrl: './ranking.html',
  styleUrl: './ranking.scss'
})
export class Ranking implements OnInit {
  rankings: RankingEntry[] = [];

  constructor(
    private router: Router,
    private userService: UserService
  ) {}

  ngOnInit(): void {
    this.loadRankings();
  }

  loadRankings(): void {
    this.rankings = this.userService.getRankings()
      .sort((a, b) => b.score - a.score || new Date(b.date).getTime() - new Date(a.date).getTime());
  }

  formatDate(dateStr: string): string {
    return new Date(dateStr).toLocaleString();
  }

  goHome(): void {
    this.router.navigate(['/']);
  }
}
