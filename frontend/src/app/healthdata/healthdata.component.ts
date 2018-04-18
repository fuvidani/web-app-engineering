import { Component, OnInit } from '@angular/core';
import {HealthdataService} from '../service/healthdata.service';
import {HealthData} from '../model/healthdata';
import {AuthService} from '../service/auth.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-healthdata',
  templateUrl: './healthdata.component.html',
  styleUrls: ['./healthdata.component.css']
})
export class HealthdataComponent implements OnInit {
  healthData = [];
  email = '';

  constructor(private healthdataService: HealthdataService, private authService: AuthService, private router: Router) { }

  ngOnInit() {
    this.healthdataService.healthData.subscribe(res => this.healthData = res);
    this.healthdataService.changeHealthData(this.healthData);
    this.email = this.authService.getPrincipal();
  }

  addHealthData() {
    //this.healthData.push(new HealthData());
    this.healthdataService.changeHealthData(this.healthData);
  }

  logOut() {
    this.authService.clearAccessToken();
    this.router.navigate(['/']);
  }

  scroll(elememt) {
    elememt.scrollIntoView({behavior:'smooth'});
  }
}
