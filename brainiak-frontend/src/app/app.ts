import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { RouterOutlet } from '@angular/router';
import { Sidebar } from './components/sidebar/sidebar';
import { Header } from './components/header/header';
import { ProfileModal } from './components/profile-modal/profile-modal';
import { ToastComponent } from './components/toast/toast';
import { UserService } from './services/user';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, Sidebar, Header, ProfileModal, ToastComponent],
  templateUrl: './app.html',
  styleUrl: './app.scss'
})
export class App {
  showProfileModal = false;

  constructor(private userService: UserService, private router: Router) {}

  openProfileModal(): void {
    const user = this.userService.getCurrentUser();
    if (user) {
      this.router.navigate(['/profile']);
    } else {
      this.showProfileModal = true;
    }
  }

  closeProfileModal(): void {
    this.showProfileModal = false;
  }
}
