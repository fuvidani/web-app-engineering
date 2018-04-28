import {Component, OnInit} from '@angular/core';
import {User} from '../model/user';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {HttpClient, HttpErrorResponse} from '@angular/common/http';
import {Router} from '@angular/router';
import {TranslateService} from '@ngx-translate/core';
import {DateAdapter} from '@angular/material';
import {Gender} from '../model/gender';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

  genders = [
    new Gender('1', 'register.gender-male', 'MALE'),
    new Gender('2', 'register.gender-female', 'FEMALE')
  ];
  error: boolean = false;

  registerForm: FormGroup;
  email: FormControl;
  password: FormControl;
  name: FormControl;
  gender: FormControl;
  birthday: FormControl;

  constructor(
    private http: HttpClient,
    private router: Router,
    private translate: TranslateService,
    private adapter: DateAdapter<any>
  ) {
  }

  ngOnInit() {
    this.configureLanguage();
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
      [Validators.required, Validators.minLength(4)]
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
    if (lang === 'de') {
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
      let user: User = this.registerForm.value;
      this.http.post<User>('http://localhost:8080/user/register', user).subscribe(
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
    this.router.navigate(['/healthdata']);
  }

  handleFailedRegistration(errorResponse: HttpErrorResponse) {
    console.error(errorResponse);
    this.error = true;
  }

  handleFinishedRegistration() {
    console.log('Finished user registration');
  }

}
