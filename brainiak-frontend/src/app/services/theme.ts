import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

export type Theme = 'dark' | 'light';

@Injectable({
  providedIn: 'root'
})
export class ThemeService {
  private themeSubject = new BehaviorSubject<Theme>(this.getStoredTheme());
  theme$ = this.themeSubject.asObservable();

  constructor() {
    this.applyTheme(this.themeSubject.value);
  }

  private getStoredTheme(): Theme {
    const saved = localStorage.getItem('theme');
    return (saved === 'light' || saved === 'dark') ? saved : 'dark';
  }

  toggleTheme(): void {
    const newTheme: Theme = this.themeSubject.value === 'dark' ? 'light' : 'dark';
    localStorage.setItem('theme', newTheme);
    this.themeSubject.next(newTheme);
    this.applyTheme(newTheme);
  }

  private applyTheme(theme: Theme): void {
    document.body.classList.remove('dark', 'light');
    document.body.classList.add(theme);
  }

  get currentTheme(): Theme {
    return this.themeSubject.value;
  }
}
