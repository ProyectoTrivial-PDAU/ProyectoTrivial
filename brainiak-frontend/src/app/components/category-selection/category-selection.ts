import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';

import { TrivialService } from '../../services/trivial';
import { ToastService } from '../../services/toast';

@Component({
  selector: 'app-category-selection',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './category-selection.html',
  styleUrl: './category-selection.scss'
})
export class CategorySelection implements OnInit {
  categories: string[] = [];
  loading = true;

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
    private toastService: ToastService,
    private cdr: ChangeDetectorRef
  ) { 
    console.log('🔧 CategorySelection - Constructor ejecutado');
  }

  ngOnInit(): void {
    console.log('🔧 CategorySelection - ngOnInit ejecutado');
    console.log('🔧 Estado inicial - loading:', this.loading);
    console.log('🔧 Estado inicial - categories:', this.categories);
    this.loadCategories();
  }

  loadCategories(): void {
    console.log('Iniciando carga de categorías...'); // Debug
    this.loading = true;
    this.trivialService.getCategorias().subscribe({
      next: (categorias) => {
        console.log('Categorías recibidas del backend:', categorias); // Debug
        console.log('Tipo de dato:', typeof categorias); // Debug
        console.log('Es array?:', Array.isArray(categorias)); // Debug
        console.log('Cantidad de categorías:', categorias.length); // Debug
        this.categories = categorias;
        this.loading = false;
        console.log('Estado final - categories:', this.categories); // Debug
        
        // 🔥 FORZAR DETECCIÓN DE CAMBIOS
        this.cdr.detectChanges();
        console.log('✅ Detección de cambios forzada');
      },
      error: (error) => {
        console.error('Error completo al cargar categorías:', error); // Debug
        this.toastService.show('Error al cargar las categorías. Verifica que el servidor esté en funcionamiento.', 'error');
        this.loading = false;
        this.cdr.detectChanges();
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

  trackCategory(index: number, category: string): string {
    return category;
  }
}
