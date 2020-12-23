import { fakeBackendProvider } from './helper/fake-backend.interceptor';
import { AuthInterceptor } from './helper/auth.interceptor';
import { ErrorInterceptor } from './helper/error.interceptor';
import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NavComponent } from './components/nav/nav.component';
import { MainComponent } from './components/main/main.component';
import { RegisterComponent } from './components/register/register.component';
import { LoginComponent } from './components/login/login.component';
import { TipsComponent } from './components/tips/tips.component';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { PlayComponent } from './components/play/play.component';
import { HomeComponent } from './components/home/home.component';
import { PlayvsComponent } from './components/playvs/playvs.component';
import { ReplayComponent } from './components/replay/replay.component';

@NgModule({
  declarations: [
    AppComponent,
    NavComponent,
    MainComponent,
    RegisterComponent,
    LoginComponent,
    TipsComponent,
    PlayComponent,
    HomeComponent,
    PlayvsComponent,
    ReplayComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule
  ],
  providers: [ { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true },
    { provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptor, multi: true }

   // provider used to create fake backend
   //,fakeBackendProvider
  ],


    bootstrap: [AppComponent]
})
export class AppModule { }
