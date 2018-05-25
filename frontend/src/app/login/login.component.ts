import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup} from '@angular/forms';
import {Authentication} from '../model/authentication';
import {AuthService} from '../service/auth.service';
import {AccessToken} from '../model/accesstoken';
import {Router} from '@angular/router';
import {HttpErrorResponse} from '@angular/common/http';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  authentication: Authentication;
  error: boolean = false;
  authorizing = false;
  loginForm: FormGroup;
  email: FormControl;
  password: FormControl;

  constructor(private authService: AuthService, private router: Router) {
  }

  ngOnInit() {
    if (this.authService.hasRole('ROLE_END_USER')) {
      this.router.navigate(['/healthdata']);
    } else if (this.authService.hasRole('ROLE_RESEARCH')) {
      this.router.navigate(['/medicalquery']);
    } else {
      this.router.navigate(['/']);
    }

    this.authentication = new Authentication();
    this.email = new FormControl(this.authentication.email);
    this.password = new FormControl(this.authentication.password);
    this.loginForm = new FormGroup({
      email: this.email,
      password: this.password,
    });
  }

  onSubmit() {
    if (this.loginForm.valid) {

      this.authentication = this.loginForm.value;
      this.handleAuthorizing();
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
    if (this.authService.hasRole('ROLE_END_USER')) {
      this.router.navigate(['/healthdata']);
    } else if (this.authService.hasRole('ROLE_RESEARCH')) {
      this.router.navigate(['/medicalquery']);
    } else {
      this.router.navigate(['/']);
    }
  }

  handleFailedAuthentication(errorResponse: HttpErrorResponse) {
    console.error(errorResponse);
    this.error = true;
    this.handleAuthorizationFinished();
  }

  handleFinishedAuthentication() {
    console.log('Successfully logged in.');
    this.handleAuthorizationFinished();
  }

  private handleAuthorizing() {
    this.enableDisableField('email', false);
    this.enableDisableField('password', false);
    this.authorizing = true;
    this.error=false;
  }

  private handleAuthorizationFinished(){
    this.enableDisableField('email', true);
    this.enableDisableField('password', true);
    this.authorizing = false;
  }


  private enableDisableField(fieldName: string, enabled) {
    if(enabled){
      this.loginForm.get(fieldName).enable()
    }else{
      this.loginForm.get(fieldName).disable()
    }
  }

}
