import { Component } from '@angular/core';
import { CommonModule, AsyncPipe } from '@angular/common';
import { ToastService, Toast as ToastModel } from '../../services/toast';

@Component({
  selector: 'app-toast',
  imports: [CommonModule, AsyncPipe],
  templateUrl: './toast.html',
  styleUrl: './toast.scss'
})
export class ToastComponent {
  toasts$;

  constructor(private toastService: ToastService) {
    this.toasts$ = this.toastService.toasts$;
  }

  removeToast(id: number): void {
    this.toastService.remove(id);
  }

  getIcon(type: string): string {
    switch (type) {
      case 'success': return '✓';
      case 'error': return '⚠';
      default: return 'ℹ';
    }
  }
}
