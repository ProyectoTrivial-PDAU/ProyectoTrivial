import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { UserService } from '../../services/user';
import { User } from '../../models/user';

@Component({
  selector: 'app-user-profile',
  imports: [CommonModule],
  templateUrl: './user-profile.html',
  styleUrl: './user-profile.scss'
})
export class UserProfile implements OnInit {
  user: User | null = null;

  constructor(
    private userService: UserService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.user = this.userService.getCurrentUser();
    if (!this.user) {
      this.router.navigate(['/']);
    }
  }

  logout(): void {
    this.userService.logout();
    this.router.navigate(['/']);
  }
}
