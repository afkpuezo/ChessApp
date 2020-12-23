import { USER_URL } from './../../environments/environment';
import { AuthService } from '@services/auth.service';
import { Injectable } from '@angular/core';
import { HttpRequest, HttpHandler, HttpEvent, HttpInterceptor } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

  constructor(private authService: AuthService) {}

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    //add heaeder with auth credentials if user is logged in and request is to the (api url)?
    const user = this.authService.userValue;
    const isLoggedIn = user && user.authdata;
    const isUrl = request.url.startsWith(USER_URL);
    if(isLoggedIn && isUrl) {
      request = request.clone({
        setHeaders: {
          Authorization: `Basic ${user.authdata}`
        }
      })
    }
    return next.handle(request);
  }
}
