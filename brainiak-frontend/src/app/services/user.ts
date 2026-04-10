import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { User, RankingEntry } from '../models/user';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  //private apiUrl = 'http://localhost:8080/api/auth';
  private apiUrl = `${environment.apiUrl}/api/auth`;
  private userSubject = new BehaviorSubject<User | null>(this.getStoredUser());
  user$ = this.userSubject.asObservable();

  constructor(private http: HttpClient) {}

  // --- LOGIN Y REGISTRO ---

  register(user: { email: string; password: string; nombre_usuario: string }): Observable<User> {
    return this.http.post<User>(`${this.apiUrl}/register`, user);
  }

  login(email: string, password: string): Observable<User> {
    return this.http.post<User>(`${this.apiUrl}/login`, { email, password });
  }

  // --- PERFIL LOCAL ---

  getStoredUser(): User | null {
    try {
      const raw = localStorage.getItem('user_profile');
      return raw ? JSON.parse(raw) : null;
    } catch {
      return null;
    }
  }

  saveUser(user: User): void {
    localStorage.setItem('user_profile', JSON.stringify(user));
    this.userSubject.next(user);
  }

  getCurrentUser(): User | null {
    return this.userSubject.value;
  }

  logout(): void {
    localStorage.removeItem('user_profile');
    this.userSubject.next(null);
  }

  // --- RANKING LOCAL ---

  getRankings(): RankingEntry[] {
    try {
      const raw = localStorage.getItem('trivial_rankings');
      return raw ? JSON.parse(raw) : [];
    } catch {
      return [];
    }
  }

  saveRanking(entry: RankingEntry): void {
    const rankings = this.getRankings();
    rankings.push(entry);
    localStorage.setItem('trivial_rankings', JSON.stringify(rankings));
  }
}
