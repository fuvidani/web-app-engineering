import { NgModule }             from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from "./home/home.component";
import { RegisterComponent } from "./register/register.component";
import { LoginComponent } from "./login/login.component";
import {HealthdataComponent} from './healthdata/healthdata.component';
import {UserGuard} from './guard/user.guard';

const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'login', component: LoginComponent },
  { path: 'healthdata', component: HealthdataComponent, canActivate: [UserGuard] }
];

@NgModule({
  imports: [
    RouterModule.forRoot(routes)
  ],
  exports: [ RouterModule ],
  declarations: []
})

export class AppRoutingModule {}
