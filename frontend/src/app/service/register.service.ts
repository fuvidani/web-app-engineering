import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {User} from '../model/user';
import {environment} from '../../environments/environment';

@Injectable()
export class RegisterService {
  baseUrl = environment.baseUrl;

  constructor(private http: HttpClient) {
  }

  register(user: User) {
    console.log('Trying to register(' + user.email + ').');
    this.http.post<User>(this.baseUrl + '/user/register', user);
  }

}
