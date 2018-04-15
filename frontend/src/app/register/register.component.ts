import { Component, OnInit } from '@angular/core';
import {User} from "../model/user";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {HttpClient} from "@angular/common/http";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

  user: User;
  registeredUser: User;
  genders = ["MALE", "FEMALE"];

  registerForm: FormGroup;
  email: FormControl;
  password: FormControl;
  name: FormControl;
  gender: FormControl;
  birthday: FormControl;

  constructor(private http: HttpClient) { }

  ngOnInit() {
    this.user = new User();
    this.email = new FormControl(
      this.user.email,
      [Validators.required, Validators.email]
    );
    this.password = new FormControl(
      this.user.password,
      [Validators.required, Validators.minLength(8)]
    );
    this.name = new FormControl(
      this.user.name,
      [Validators.required, Validators.minLength(4)]
    );
    this.gender = new FormControl(
      this.user.gender,
      [Validators.required]
    );
    this.birthday = new FormControl(
      this.user.birthday,
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

  onSubmit() {
    if(this.registerForm.valid){
      this.user = this.registerForm.value;
      this.http.post<User>("http://localhost:8080/user/register", this.user).subscribe(
        registeredUser => {
          this.registeredUser = registeredUser;
        },
        err => {
          console.error(err);
        },
        () => console.log("Successfully registered user(" + this.registeredUser.email + ")")
      );
    }
  }

}
