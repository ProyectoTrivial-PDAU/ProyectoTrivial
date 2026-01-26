import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { UserService } from '../../services/user';
import { RankingEntry } from '../../models/user';
import { environment } from '../../../environments/environment';


@Component({
  selector: 'app-ranking',
  imports: [CommonModule],
  templateUrl: './ranking.html',
  styleUrl: './ranking.scss',
})
export class Ranking implements OnInit {
  rankings: RankingEntry[] = [];
  rankingGlobal: any[] = [];
  loading = false;
  error = false;

  constructor(
    private router: Router,
    private userService: UserService,
    private http: HttpClient,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void {
    this.loadRankings();
    this.loadRankingGlobal();
  }

  loadRankings(): void {
    this.rankings = this.userService.getRankings()
      .sort((a, b) => b.score - a.score || new Date(b.date).getTime() - new Date(a.date).getTime());
  }

  loadRankingGlobal(): void {
    this.loading = true;
    this.error = false;
    
    console.log('🔍 Cargando ranking global...');
    
    // Usar HttpClient en lugar de fetch para mejor integración con Angular
    //this.http.get<any[]>('/api/trivial/ranking-global').subscribe({
    this.http.get<any[]>(`${environment.apiUrl}/api/trivial/ranking-global`).subscribe({
      next: (data) => {
        console.log('✅ Ranking global recibido:', data);
        console.log('📊 Primer registro (para debug):', data[0]);
        console.log('🔑 Campos del primer registro:', data[0] ? Object.keys(data[0]) : 'No hay datos');
        this.rankingGlobal = data;
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('❌ Error cargando ranking global:', err);
        this.error = true;
        this.loading = false;
        this.cdr.detectChanges();
      }
    });
  }

  // Métodos auxiliares para manejar diferentes formatos de campos
  getPlayerName(entry: any): string {
    return entry.nombreJugador || entry.name || entry.jugador || 'Sin nombre';
  }

  getScore(entry: any): number {
    return entry.puntuacion ?? entry.score ?? 0;
  }

  getTotal(entry: any): number {
    return entry.totalPreguntas ?? entry.total ?? 5;
  }

  getCategory(entry: any): string {
    return entry.categoria || entry.category || 'Sin categoría';
  }

  getFormattedDate(entry: any): string {
    const dateStr = entry.fechaPartida || entry.fecha || entry.date;
    if (!dateStr) return 'Sin fecha';
    return this.formatDate(dateStr);
  }

  formatDate(dateStr: string): string {
    try {
      return new Date(dateStr).toLocaleString('es-ES', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit'
      });
    } catch {
      return 'Fecha inválida';
    }
  }

  goHome(): void {
    this.router.navigate(['/']);
  }
}