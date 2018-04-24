import { Component, OnInit } from '@angular/core';
import {AuthService} from "../service/auth.service";
import {Router} from "@angular/router";
import {MedicalQuery} from "../model/medicalquery";
import {MedicalqueryService} from "../service/medicalquery.service";

@Component({
  selector: 'app-medicalquery',
  templateUrl: './medicalquery.component.html',
  styleUrls: ['./medicalquery.component.css']
})
export class MedicalqueryComponent implements OnInit {

  email: string;
  test = new MedicalQuery(2, "TU Vienna", "Abfrage 1", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.", 10, 50, 60, "FEMALE", ["cancer", "skin"]);

  constructor(
    private authService: AuthService,
    private medicalQueryService: MedicalqueryService,
    private router: Router
  ) { }

  ngOnInit() {
    this.email = this.authService.getPrincipal().email;
  }

  scroll() {
    //elememt.scrollIntoView({behavior:'smooth'});
    this.medicalQueryService.addMedicalQuery(this.test);
  }

  logOut() {
    this.authService.clearAccessToken();
    this.router.navigate(['/']);
  }

  navigateToSharedHealthdata(qid: string){
    this.router.navigate(['medicalquery', qid, 'shared']);
  }

}
