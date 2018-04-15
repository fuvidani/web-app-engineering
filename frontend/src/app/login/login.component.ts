import { Component, OnInit } from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {Authentication} from "../model/authentication";
import {AuthService} from "../service/auth.service";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  authentication: Authentication;

  loginForm: FormGroup;
  email: FormControl;
  password: FormControl;

  constructor(private authService: AuthService) { }

  ngOnInit() {
    this.authentication = new Authentication();
    this.email = new FormControl(this.authentication.email);
    this.password = new FormControl(this.authentication.password);
    this.loginForm = new FormGroup({
      email: this.email,
      password: this.password,
    });
  }

  onSubmit() {
    if(this.loginForm.valid){
     this.authentication = this.loginForm.value;
     console.log(this.authentication);
     this.authService.login(this.authentication);
    }
  }

}
