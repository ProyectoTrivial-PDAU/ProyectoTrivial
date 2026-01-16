import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Sidebar } from './components/sidebar/sidebar';
import { Header } from './components/header/header';
import { ProfileModal } from './components/profile-modal/profile-modal';
import { ToastComponent } from './components/toast/toast';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, Sidebar, Header, ProfileModal, ToastComponent],
  templateUrl: './app.html',
  styleUrl: './app.scss'
})
export class App {
  showProfileModal = false;

  openProfileModal(): void {
    this.showProfileModal = true;
  }

  closeProfileModal(): void {
    this.showProfileModal = false;
  }
}
