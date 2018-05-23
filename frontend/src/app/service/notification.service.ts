import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {environment} from "../../environments/environment";

@Injectable()
export class NotificationService {
  baseUrl = environment.baseUrl;


  constructor(private http: HttpClient) { }

  addPushSubscriber(sub:any){
    return this.http.post(this.baseUrl + '/notifications', sub);
  }
}
