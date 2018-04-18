import { Component, OnInit } from '@angular/core';
import {AuthService} from "../service/auth.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-medicalquery',
  templateUrl: './medicalquery.component.html',
  styleUrls: ['./medicalquery.component.css']
})
export class MedicalqueryComponent implements OnInit {

  email: string;

  constructor(private authService: AuthService, private router: Router) { }

  ngOnInit() {
    this.email = this.authService.getPrincipal();
  }

  scroll(elememt) {
    elememt.scrollIntoView({behavior:'smooth'});
  }

  logOut() {
    this.authService.clearAccessToken();
    this.router.navigate(['/']);
  }

}
