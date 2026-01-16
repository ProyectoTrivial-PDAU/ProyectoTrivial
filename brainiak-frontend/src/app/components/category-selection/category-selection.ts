import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { TrivialService } from '../../services/trivial';
import { ToastService } from '../../services/toast';

@Component({
  selector: 'app-category-selection',
  imports: [CommonModule],
  templateUrl: './category-selection.html',
  styleUrl: './category-selection.scss'
})
export class CategorySelection implements OnInit {
  categories: string[] = [];
  
  categoryIcons: { [key: string]: string } = {
    'Matemáticas': '🔢',
    'Geografía': '🌍',
    'Historia': '📜',
    'Ciencia': '🔬',
    'Arte': '🎨'
  };

  constructor(
    private router: Router,
    private trivialService: TrivialService,
    private toastService: ToastService
  ) {}

  ngOnInit(): void {
    this.loadCategories();
  }

  loadCategories(): void {
    this.trivialService.getCategorias().subscribe({
      next: (categorias) => {
        this.categories = categorias;
      },
      error: () => {
        this.toastService.show('Error al cargar las categorías.', 'error');
      }
    });
  }

  getIcon(category: string): string {
    return this.categoryIcons[category] || '📚';
  }

  selectCategory(category: string): void {
    this.router.navigate(['/game'], { 
      queryParams: { mode: 'category', category } 
    });
  }

  goHome(): void {
    this.router.navigate(['/']);
  }
}
