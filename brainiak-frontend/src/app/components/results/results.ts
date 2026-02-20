import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-results',
  imports: [],
  templateUrl: './results.html',
  styleUrl: './results.scss'
})
export class Results implements OnInit {
  score = 0;
  total = 0;
  gameMode = 'random';
  category = '';

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
    });
  }

  restartGame(): void {
    if (this.gameMode === 'category') {
      this.router.navigate(['/game'], { 
        queryParams: { mode: 'category', category: this.category } 
      });
    } else {
      this.router.navigate(['/game'], { queryParams: { mode: 'random' } });
    }
  }

  goHome(): void {
    this.router.navigate(['/']);
  }
}
