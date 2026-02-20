import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

export interface Toast {
  id: number;
  message: string;
  type: 'info' | 'success' | 'error';
}

@Injectable({
  providedIn: 'root'
})
export class ToastService {
  private toastsSubject = new BehaviorSubject<Toast[]>([]);
  toasts$ = this.toastsSubject.asObservable();
  private counter = 0;

  show(message: string, type: 'info' | 'success' | 'error' = 'info', duration = 3500): void {
    const toast: Toast = { id: ++this.counter, message, type };
    const current = this.toastsSubject.value;
    this.toastsSubject.next([...current, toast]);

    setTimeout(() => this.remove(toast.id), duration);
  }

  remove(id: number): void {
    const current = this.toastsSubject.value;
    this.toastsSubject.next(current.filter(t => t.id !== id));
  }
}
