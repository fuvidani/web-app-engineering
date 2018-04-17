import { Component, OnInit } from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {Authentication} from "../model/authentication";
import {AuthService} from "../service/auth.service";
import {AccessToken} from "../model/accesstoken";
import {Router} from "@angular/router";
import {HttpErrorResponse} from "@angular/common/http";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  authentication: Authentication;
  error: boolean = false;

  loginForm: FormGroup;
  email: FormControl;
  password: FormControl;

  constructor(private authService: AuthService, private router: Router) { }

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
      this.authService.authenticate(this.authentication).subscribe(
        accessToken => {
          this.handleSuccessFullAuthentication(accessToken);
        },
        err => {
          this.handleFailedAuthentication(err);
        },
        () => {
          this.handleFinishedAuthentication();
        }
      );
    }
  }

  handleSuccessFullAuthentication(accessToken: AccessToken) {
    this.authService.setAccessToken(accessToken.token);
    this.router.navigate(['/']);
  }

  handleFailedAuthentication(errorResponse: HttpErrorResponse) {
    console.error(errorResponse);
    this.error = true;
  }

  handleFinishedAuthentication() {
    console.log("Successfully logged in.")
  }

}
