import { ReplayComponent } from './components/replay/replay.component';
import { AuthGuard } from './helper/auth.guard';
import { HomeComponent } from './components/home/home.component';
import { PlayComponent } from './components/play/play.component';
import { PlayvsComponent } from './components/playvs/playvs.component';
import { TipsComponent } from './components/tips/tips.component';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { MainComponent } from './components/main/main.component';


import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

const routes: Routes = [
  {path: '', component: MainComponent }
  ,{path: 'main', component: MainComponent}
  ,{path: 'register', component: RegisterComponent}
  ,{path: 'login', component: LoginComponent}
  ,{path: 'tips', component: TipsComponent}
  ,{path: 'play', component: PlayComponent}
  ,{path: 'home', component: HomeComponent, canActivate: [AuthGuard]}
  ,{path: 'playvs', component: PlayvsComponent}
  ,{path: 'replay', component: ReplayComponent}
  ,{path: '**', redirectTo: ''}
  
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
