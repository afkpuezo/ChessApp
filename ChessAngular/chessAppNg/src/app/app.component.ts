import { AuthService } from '@services/auth.service';
import { User } from '@app/models/user.model';
import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'chessAppNg';
  user: User;

    constructor(
        private router: Router,
        private authService: AuthService
    ) {
        this.authService.user.subscribe(x => this.user = x);
    }

    logout() {
        this.authService.logout();
    }
}
