import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import { JwtHelperService } from "@auth0/angular-jwt";
import {Authentication} from "../model/authentication";
import {AccessToken} from "../model/accesstoken";
import {DecodedAccessToken} from "../model/decodedaccesstoken";

@Injectable()
export class AuthService {

  jwtHelper = new JwtHelperService();

  constructor(private http: HttpClient) { }

  isAuthenticated() {
    console.log("Checking if user is authenticated.");
    const access_token: string = localStorage.getItem("access_token");

    if(access_token == null){
      console.log("User is not authenticated. No access token.");
      return false;
    }

    if(this.jwtHelper.isTokenExpired(access_token)){
      console.log("User is not authenticated. The token is expired.");
      return false;
    }

    console.log("User is authenticated.");
    return true;
  }

  getPrincipal(){
    console.log("Fetching the currently authenticated subject.")
    if(!this.isAuthenticated()){
      return null;
    }

    const access_token: string = localStorage.getItem("access_token");
    const decodedToken: DecodedAccessToken = this.jwtHelper.decodeToken(access_token);

    return decodedToken;
  }

  hasRole(role: string) {
    console.log("Checking if user is authorized with role(" + role + ")");

    if(!this.isAuthenticated()){
      return false;
    }

    const access_token: string = localStorage.getItem("access_token");
    const decodedToken: DecodedAccessToken = this.jwtHelper.decodeToken(access_token);

    if(!decodedToken.roles.includes(role)){
      console.log("User is not authorized with role(" + role + ")");
      return false;
    }

    console.log("User is authorized with role(" + role + ")");
    return true;
  }

  setAccessToken(accessToken: string) {
    console.log("Saving access token in local storage.");
    localStorage.setItem("access_token", accessToken);
  }

  clearAccessToken() {
    console.log("Removing access token from local storage.");
    localStorage.removeItem("access_token");
  }

  authenticate(auth: Authentication) {
    console.log("Trying to authenticate(" + auth.email + ").")
    return this.http.post<AccessToken>("http://localhost:8080/auth", auth);
  }

}
