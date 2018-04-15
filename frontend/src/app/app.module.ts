import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';


import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MaterialModule } from './material.module';
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { AboutComponent } from './about/about.component';
import { HomeComponent } from './home/home.component';
import { AppRoutingModule } from "./app-routing.module";
import { RegisterComponent } from './register/register.component';
import {HttpClientModule} from "@angular/common/http";
import { LoginComponent } from './login/login.component';
import {AuthService} from "./service/auth.service";
import {AuthGuard} from "./guard/auth.guard";
import {UserGuard} from "./guard/user.guard";
import {ResearchfacilityGuard} from "./guard/researchfacility.guard";


@NgModule({
  declarations: [
    AppComponent,
    AboutComponent,
    HomeComponent,
    RegisterComponent,
    LoginComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    MaterialModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    AppRoutingModule
  ],
  providers: [
    AuthService,
    AuthGuard,
    UserGuard,
    ResearchfacilityGuard
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
