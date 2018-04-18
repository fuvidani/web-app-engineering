import { Component, OnInit } from '@angular/core';
import {AuthService} from "../service/auth.service";
import {Router} from "@angular/router";
import {MedicalQuery} from "../model/medicalquery";

@Component({
  selector: 'app-medicalquery',
  templateUrl: './medicalquery.component.html',
  styleUrls: ['./medicalquery.component.css']
})
export class MedicalqueryComponent implements OnInit {

  email: string;
  data: MedicalQuery;

  constructor(private authService: AuthService, private router: Router) { }

  ngOnInit() {
    this.email = this.authService.getPrincipal();
    this.data = new MedicalQuery(
      1,
      "1234",
      "Abfrage 1",
      "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
      500,
      18,
      25,
      "MALE",
      ["tag1", "tag2"]
    );
  }

  scroll(elememt) {
    elememt.scrollIntoView({behavior:'smooth'});
  }

  logOut() {
    this.authService.clearAccessToken();
    this.router.navigate(['/']);
  }

}
