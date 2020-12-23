import { AuthService } from '@services/auth.service';
import { Injectable } from '@angular/core';
import { Router, CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {
  constructor(
    private router: Router,
    private authService: AuthService
  ) { }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
    
    const user = this.authService.userValue;

    if (user) {
      //logged in.
      return true;
    }

    this.router.navigate(['login'],{queryParams: {returnUrl: state.url}});
    return false;
  }

}