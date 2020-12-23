import { AuthService } from '@services/auth.service';
import { User } from '@app/models/user.model';
import { Component } from '@angular/core';


@Component({
  selector: 'app-nav',
  templateUrl: './nav.component.html',
  styleUrls: ['./nav.component.css']
})

export class NavComponent {
  user: User;
  crown = "./assets/images/Crown.png"
  constructor(
    private authService: AuthService
) {
    this.authService.user.subscribe(x => this.user = x);
}

logout() {
    this.authService.logout();
}

}
