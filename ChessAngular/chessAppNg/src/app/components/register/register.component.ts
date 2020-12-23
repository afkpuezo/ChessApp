import { User } from '@models/user.model';
import { UserService } from '@services/user.service';
import { ClientMessage } from '@models/client-message.model';
import { Component, OnInit } from '@angular/core';
import { FormGroup, ReactiveFormsModule, Validators, FormBuilder } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

  user: User;
  registerForm: FormGroup;
    loading = false;
    submitted = false;
    returnUrl: string;
    error = '';
  
  
  title = 'Register New Player';
  
  // Constructor Injection
  constructor(private userService: UserService,
    private formBuilder: FormBuilder,
    private route: ActivatedRoute,
    private router: Router) { 
      
    }
  ngOnInit(): void {
    
      this.registerForm = this.formBuilder.group({
        username: ['', Validators.required],
        password: ['', Validators.required],
        email: ['', Validators.required]
      });
    this.returnUrl = this.route.snapshot.queryParamMap[''] || '/login';
  }


  // Message to the user
  public clientMessage: ClientMessage = new ClientMessage('');

  get f() {
    return this.registerForm.controls;}

    onSubmit() {
    if (this.registerForm.invalid) {
      return;
  }
    this.user = new User(0,this.f.username.value, this.f.password.value, this.f.email.value);
  this.submitted = true;
    console.log(this.user);
    this.userService.registerUser(this.user)
      .subscribe(
        data => {
          this.clientMessage,
          this.router.navigate([this.returnUrl])},
        error => this.clientMessage.message = 'SOMETHING WENT WRONG'
      )
  }
}

