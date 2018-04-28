import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {User} from "../model/user";

@Injectable()
export class RegisterService {

  constructor(private http: HttpClient) { }

  register(user: User) {
    console.log("Trying to register(" + user.email + ").")
    this.http.post<User>("http://localhost:8080/user/register", user);
  }

}
