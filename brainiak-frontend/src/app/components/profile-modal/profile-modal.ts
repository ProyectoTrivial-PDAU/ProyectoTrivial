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

  isRegisterMode = false;
  email = '';
  password = '';
  username = '';
  confirmPassword = '';
  error = '';
  name = ''; 
  nickname = '';

  constructor(
    private userService: UserService,
    private toastService: ToastService
  ) {}

  onOverlayClick(event: Event): void {
    if (event.target === event.currentTarget) {
      this.closeModal();
    }
  }

  closeModal(): void {
    this.close.emit();
    this.resetForm();
  }

  toggleMode(): void {
    this.isRegisterMode = !this.isRegisterMode;
    this.error = '';
    this.resetForm();
  }

  resetForm(): void {
    this.email = '';
    this.password = '';
    this.username = '';
    this.confirmPassword = '';
    this.error = '';
  }

  submit(): void {
    this.error = '';
    
    if (!this.email || !this.password) {
      this.error = 'Por favor completa todos los campos';
      return;
    }

    if (this.isRegisterMode) {
      if (!this.username) {
        this.error = 'El nombre de usuario es obligatorio';
        return;
      }
      if (this.password !== this.confirmPassword) {
        this.error = 'Las contraseñas no coinciden';
        return;
      }
      this.register();
    } else {
      this.login();
    }
  }

  login(): void {
    this.userService.login(this.email, this.password).subscribe({
      next: (user) => {
        this.userService.saveUser(user);
        this.toastService.show(`Bienvenido, ${user.nombre_usuario}`, 'success');
        this.closeModal();
      },
      error: (err) => {
        console.error(err);
        const serverMsg = err.error;
        if (typeof serverMsg === 'string' && serverMsg.length > 0) {
          this.error = serverMsg;
        } else {
          this.error = 'Credenciales inválidas o error en el servidor';
        }
        this.toastService.show(this.error, 'error');
      }
    });
  }

  register(): void {
    const user = { 
      email: this.email, 
      password: this.password, 
      nombre_usuario: this.username 
    };
    
    this.userService.register(user).subscribe({
      next: (newUser) => {
        this.userService.saveUser(newUser);
        this.toastService.show('Cuenta creada exitosamente', 'success');
        this.closeModal();
      },
      error: (err) => {
        console.error(err);
        // Mostrar el mensaje real del backend si existe
        const serverMsg = err.error;
        if (typeof serverMsg === 'string' && serverMsg.length > 0) {
          this.error = serverMsg;
        } else {
          this.error = 'Error al crear cuenta. Verifica los datos e inténtalo de nuevo.';
        }
        this.toastService.show(this.error, 'error');
      }
    });
  }
}
