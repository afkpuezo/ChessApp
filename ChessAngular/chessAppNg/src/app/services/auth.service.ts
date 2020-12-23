import { USER_URL } from './../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { User } from '@models/user.model';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { Router } from '@angular/router';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private userSubject: BehaviorSubject<User>;
  public user: Observable<User>; 
  //Getter
  
  constructor(
    private router: Router,
    private http: HttpClient
  ) { 
    this.userSubject = new BehaviorSubject<User>(JSON.parse(localStorage.getItem('user')));  
    this.user = this.userSubject.asObservable();
  }

  public get userValue(): User {
    return this.userSubject.value;
  }

  login(username: string, password: string) {
    return this.http.post<User>(`${USER_URL}logIn`, [username,password])
    .pipe(map(user => { 
      user.authdata = window.btoa(username+':'+password);
      localStorage.setItem('user',JSON.stringify(user));
      this.userSubject.next(user);
      return user;
    }));
  }

  logout() {
    //remove user from local storage to log user out
    localStorage.removeItem('user');
    this.userSubject.next(null);
    this.router.navigate(['/login']);
  }
}
