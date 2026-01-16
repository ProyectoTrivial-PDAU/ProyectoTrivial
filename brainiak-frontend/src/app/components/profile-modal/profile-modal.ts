import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { UserService } from '../../services/user';
import { ToastService } from '../../services/toast';

@Component({
  selector: 'app-profile-modal',
  imports: [CommonModule, FormsModule],
  templateUrl: './profile-modal.html',
  styleUrl: './profile-modal.scss'
})
export class ProfileModal {
  @Input() isOpen = false;
  @Output() close = new EventEmitter<void>();

  name = '';
  nickname = '';
  error = '';

  constructor(
    private userService: UserService,
    private toastService: ToastService
  ) {
    const user = this.userService.getStoredUser();
    if (user) {
      this.name = user.name || '';
      this.nickname = user.nickname || '';
    }
  }

  onOverlayClick(event: Event): void {
    if (event.target === event.currentTarget) {
      this.closeModal();
    }
  }

  closeModal(): void {
    this.close.emit();
  }

  async saveProfile(): Promise<void> {
    if (!this.name.trim()) {
      this.error = 'El nombre no puede estar vacío.';
      this.toastService.show('El nombre no puede estar vacío.', 'error');
      return;
    }

    const user = { name: this.name.trim(), nickname: this.nickname.trim() };
    
    // Try to save to backend
    let savedOnServer = false;
    try {
      await this.userService.saveUserToServer(user).toPromise();
      savedOnServer = true;
    } catch {
      // Ignore network errors
    }

    this.userService.saveUser(user);
    this.closeModal();

    if (savedOnServer) {
      this.toastService.show('Perfil guardado y sincronizado con servidor.', 'success');
    } else {
      this.toastService.show('Perfil guardado localmente (sincronización pendiente).', 'info');
    }
  }
}
