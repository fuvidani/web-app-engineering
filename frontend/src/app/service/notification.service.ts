import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {environment} from "../../environments/environment";
import {AuthService} from "./auth.service";

@Injectable()
export class NotificationService {
  baseUrl = environment.baseUrl;


  constructor(private http: HttpClient, private authService: AuthService) { }

  addPushSubscriber(sub:any){
    const payload = {
      email: this.authService.getPrincipal().email,
      subscription: sub
    }
    return this.http.post(this.baseUrl + '/notifications', payload);
  }
}
