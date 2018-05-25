import {Component, OnInit} from '@angular/core';
import {User} from '../model/user';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {HttpClient, HttpErrorResponse} from '@angular/common/http';
import {Router} from '@angular/router';
import {TranslateService} from '@ngx-translate/core';
import {DateAdapter} from '@angular/material';
import {Gender} from '../model/gender';
import {DatePipe} from "@angular/common";
import {environment} from '../../environments/environment';
import {AuthService} from '../service/auth.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {
  baseUrl = environment.baseUrl;

  genders = [
    new Gender('1', 'register.gender-male', 'MALE'),
    new Gender('2', 'register.gender-female', 'FEMALE')
  ];
  error: boolean = false;
  registering = false;
  registerForm: FormGroup;
  email: FormControl;
  password: FormControl;
  name: FormControl;
  gender: FormControl;
  birthday: FormControl;

  datePipe: DatePipe;

  constructor(
    private http: HttpClient,
    private router: Router,
    private translate: TranslateService,
    private adapter: DateAdapter<any>,
    private authService: AuthService
  ) {
  }

  ngOnInit() {
    if (this.authService.hasRole('ROLE_END_USER')) {
      this.router.navigate(['/healthdata']);
    } else if (this.authService.hasRole('ROLE_RESEARCH')) {
      this.router.navigate(['/medicalquery']);
    }

    this.configureLanguage();
    this.datePipe = new DatePipe('en');
    this.email = new FormControl(
      '',
      [Validators.required, Validators.email]
    );
    this.password = new FormControl(
      '',
      [Validators.required, Validators.minLength(8)]
    );
    this.name = new FormControl(
      '',
      [Validators.required, Validators.minLength(1)]
    );
    this.gender = new FormControl(
      '',
      [Validators.required]
    );
    this.birthday = new FormControl(
      '',
      [Validators.required]
    );
    this.registerForm = new FormGroup({
      email: this.email,
      password: this.password,
      name: this.name,
      gender: this.gender,
      birthday: this.birthday
    });
  }

  configureLanguage() {
    const lang = window.navigator.language;
    console.log('Detected browser language(' + lang + ')');
    if (lang === 'de' || lang === 'de-DE') {
      this.translate.setDefaultLang('de');
      this.adapter.setLocale('de');
      console.log('Set language to german');
    } else {
      this.translate.setDefaultLang('en');
      console.log('Set language to english');
    }
  }

  onSubmit() {
    if (this.registerForm.valid) {
      this.handleRegistering();
      let user: User = this.registerForm.value;
      user.birthday = this.datePipe.transform(user.birthday, 'dd-MM-yyyy');
      this.http.post<User>(this.baseUrl + '/user/register', user).subscribe(
        registeredUser => {
          this.handleSuccessFullRegistration();
        },
        err => {
          this.handleFailedRegistration(err);
        },
        () => {
          this.handleFinishedRegistration();
        }
      );
    }
  }

  handleSuccessFullRegistration() {
    console.log('Successfully registered user.');
    this.router.navigate(['/login']);
  }

  handleFailedRegistration(errorResponse: HttpErrorResponse) {
    console.error(errorResponse);
    this.error = true;
    this.handleRegistrationFinished();
  }

  handleFinishedRegistration() {
    console.log('Finished user registration');
    this.handleRegistrationFinished();
  }

  private handleRegistering() {
    this.enableDisableField('email', false);
    this.enableDisableField('password', false);
    this.enableDisableField('name', false);
    this.enableDisableField('gender', false);
    this.enableDisableField('birthday', false);
    this.registering = true;
    this.error=false;
  }

  private handleRegistrationFinished(){
    this.enableDisableField('email', true);
    this.enableDisableField('password', true);
    this.enableDisableField('name', true);
    this.enableDisableField('gender', true);
    this.enableDisableField('birthday', true);
    this.registering = false;
  }


  private enableDisableField(fieldName: string, enabled) {
    if(enabled){
      this.registerForm.get(fieldName).enable()
    }else{
      this.registerForm.get(fieldName).disable()
    }
  }
}
