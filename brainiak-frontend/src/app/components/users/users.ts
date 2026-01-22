import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { UserService } from '../../services/user';
import { ToastService } from '../../services/toast';
import { User } from '../../models/user';

@Component({
  selector: 'app-users',
  imports: [CommonModule],
  templateUrl: './users.html',
  styleUrl: './users.scss'
})
export class Users implements OnInit {
  users: User[] = [];
  loading = true;
  error = false;

  constructor(
    private router: Router,
    private userService: UserService,
    private toastService: ToastService
  ) {}

  ngOnInit(): void {
    this.loadUsers();
  }

  loadUsers(): void {
    this.loading = true;
    this.error = false;
    /*
    this.userService.getUsers().subscribe({
      next: (users) => {
        this.users = users;
        this.loading = false;
      },
      error: () => {
        this.error = true;
        this.loading = false;
        this.toastService.show('Error cargando usuarios desde el servidor', 'error');
      }
    });
    */
  }

  /*
  deleteUser(user: User): void {
    if (!confirm(`¿Eliminar usuario ${user.name}?`)) return;
    
    if (user.id) {
      this.userService.deleteUser(user.id).subscribe({
        next: () => {
          this.users = this.users.filter(u => u.id !== user.id);
          this.toastService.show('Usuario eliminado', 'success');
        },
        error: () => {
          this.toastService.show('No se pudo eliminar en el servidor', 'error');
        }
      });
    }
  }
  */

  goHome(): void {
    this.router.navigate(['/']);
  }
}
