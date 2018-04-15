import { Component, OnInit } from '@angular/core';
import {User} from "../model/user";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

  user: User;
  genders = ["Male", "Female"];

  constructor() { }

  ngOnInit() {
    this.user = new User();
  }

  onSubmit() {
    console.log(this.user);
  }

}
