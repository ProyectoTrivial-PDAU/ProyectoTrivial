import { Component, OnDestroy, OnInit, ChangeDetectorRef } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { CommonModule } from '@angular/common';
import { A11yModule } from '@angular/cdk/a11y';
import { Dialog } from '@angular/cdk/dialog';
import { DialogModule } from '@angular/cdk/dialog';
import { BreakpointObserver } from '@angular/cdk/layout';
import { ScrollingModule } from '@angular/cdk/scrolling';
import { Subscription } from 'rxjs';

import { TrivialService } from '../../services/trivial';
import { ToastService } from '../../services/toast';
import { QuestionCountDialog } from '../question-count-dialog/question-count-dialog';

@Component({
  selector: 'app-category-selection',
  standalone: true,
  imports: [CommonModule, ScrollingModule, A11yModule, DialogModule],
  templateUrl: './category-selection.html',
  styleUrl: './category-selection.scss'
})
export class CategorySelection implements OnInit, OnDestroy {
  categories: string[] = [];
  categoryRows: string[][] = [];
  loading = true;
  selectedCount = 5;
  visibleColumns = 4;
  readonly rowHeight = 92;
  private breakpointSub?: Subscription;

  categoryIcons: { [key: string]: string } = {
    'Matemáticas': '🔢',
    'Geografía': '🌍',
    'Historia': '📜',
    'Ciencia': '🔬',
    'Arte': '🎨',
    'Conocimiento General': '🧠',
    'Ciencia y Naturaleza': '🌿',
    'Ciencia: Computación': '💻',
    'Deportes': '⚽',
    'Cine': '🎬',
    'Música': '🎵',
    'Videojuegos': '🎮',
    'Animación y Manga': '🎌',
    'Animales': '🐾',
    'Mitología': '⚡',
    'Celebridades': '🌟',
    'Cómics': '💥',
    'Gadgets': '📱',
    'Política': '🏛️',
    'Vehículos': '🚗',
    'Televisión': '📺',
    'Libros': '📖',
    'Teatro': '🎭'
  };

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private trivialService: TrivialService,
    private toastService: ToastService,
    private cdr: ChangeDetectorRef,
    private breakpointObserver: BreakpointObserver,
    private dialog: Dialog
  ) {}

  ngOnInit(): void {
    this.route.queryParams.subscribe((params) => {
      this.selectedCount = Number(params['count']) || 5;
    });
    this.observeColumns();
    this.loadCategories();
  }

  ngOnDestroy(): void {
    this.breakpointSub?.unsubscribe();
  }

  private observeColumns(): void {
    this.breakpointSub = this.breakpointObserver
      .observe(['(max-width: 700px)', '(max-width: 1024px)', '(max-width: 1280px)'])
      .subscribe((state) => {
        if (state.breakpoints['(max-width: 700px)']) {
          this.visibleColumns = 1;
        } else if (state.breakpoints['(max-width: 1024px)']) {
          this.visibleColumns = 2;
        } else if (state.breakpoints['(max-width: 1280px)']) {
          this.visibleColumns = 3;
        } else {
          this.visibleColumns = 4;
        }

        this.rebuildRows();
        this.cdr.detectChanges();
      });
  }

  private rebuildRows(): void {
    this.categoryRows = [];

    for (let index = 0; index < this.categories.length; index += this.visibleColumns) {
      this.categoryRows.push(this.categories.slice(index, index + this.visibleColumns));
    }
  }

  loadCategories(): void {
    this.loading = true;
    this.trivialService.getCategorias().subscribe({
      next: (categorias) => {
        this.categories = categorias;
        this.rebuildRows();
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: (error) => {
        console.error('Error completo al cargar categorías:', error);
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
      queryParams: { mode: 'category', category, count: this.selectedCount }
    });
  }

  openCountModal(category: string): void {
    const dialogRef = this.dialog.open<number>(QuestionCountDialog, {
      data: {
        title: category,
        subtitle: '¿Cuántas preguntas quieres?',
        emoji: this.getIcon(category),
        initialCount: this.selectedCount,
        min: 5,
        max: 20,
        playLabel: '🚀 ¡Jugar!'
      },
      panelClass: 'brainiak-dialog-panel',
      backdropClass: 'brainiak-dialog-backdrop'
    });

    dialogRef.closed.subscribe((count) => {
      if (!count) return;
      this.selectedCount = count;
      this.router.navigate(['/game'], {
        queryParams: { mode: 'category', category, count }
      });
    });
  }

  goHome(): void {
    this.router.navigate(['/']);
  }

  trackCategory(index: number, category: string): string {
    return category;
  }

  trackCategoryRow(index: number, row: string[]): string {
    return row.join('|');
  }
}
