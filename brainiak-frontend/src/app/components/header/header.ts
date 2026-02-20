import { Component, Output, EventEmitter, OnInit, OnDestroy } from '@angular/core';
import { UserService } from '../../services/user';
import { User } from '../../models/user';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-header',
  imports: [],
  templateUrl: './header.html',
  styleUrl: './header.scss'
})
export class Header implements OnInit, OnDestroy {
  @Output() openProfile = new EventEmitter<void>();
  
  user: User | null = null;
  private subscription?: Subscription;

  constructor(private userService: UserService) {}

  ngOnInit(): void {
    this.subscription = this.userService.user$.subscribe(user => {
      this.user = user;
    });
  }

  ngOnDestroy(): void {
    this.subscription?.unsubscribe();
  }

  get displayName(): string {
    if (!this.user) return 'Iniciar sesión / Registro';
    return this.user.nombre_usuario || this.user.name || this.user.nickname || 'Usuario';
  }

  get avatar(): string {
    const name = this.user?.nombre_usuario || this.user?.name || this.user?.nickname;
    if (!name) return '👤';
    const initials = name
      .trim()
      .split(' ')
      .map(s => s[0])
      .slice(0, 2)
      .join('')
      .toUpperCase();
    return initials || '👤';
  }
}
