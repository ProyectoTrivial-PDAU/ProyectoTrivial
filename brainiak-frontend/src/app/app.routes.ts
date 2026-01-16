import { Routes } from '@angular/router';
import { Home } from './components/home/home';
import { Game } from './components/game/game';
import { Results } from './components/results/results';
import { CategorySelection } from './components/category-selection/category-selection';
import { Ranking } from './components/ranking/ranking';
import { Users } from './components/users/users';

export const routes: Routes = [
  { path: '', component: Home },
  { path: 'game', component: Game },
  { path: 'results', component: Results },
  { path: 'categories', component: CategorySelection },
  { path: 'ranking', component: Ranking },
  { path: 'users', component: Users },
  { path: '**', redirectTo: '' }
];
