import { Component, OnInit } from '@angular/core';
import {HealthData} from "../../model/healthdata";
import {AuthService} from "../../service/auth.service";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-shared-healthdata',
  templateUrl: './shared-healthdata.component.html',
  styleUrls: ['./shared-healthdata.component.css']
})
export class SharedHealthdataComponent implements OnInit {

  email: string;
  qid: string;
  data = new HealthData('Brust', 'Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.', 'http://st1.thehealthsite.com/wp-content/uploads/2017/04/Worst-things-that-can-happen-to-your-breasts.jpg', ['breast', 'titties', 'bh']);

  constructor(
    private authService: AuthService,
    private router: Router,
    private route: ActivatedRoute
  ) { }

  ngOnInit() {
    this.email = this.authService.getPrincipal().email;
    this.route.params.subscribe( params => this.qid = params['qid']);
  }

  logOut() {
    this.authService.clearAccessToken();
    this.router.navigate(['/']);
  }

}
