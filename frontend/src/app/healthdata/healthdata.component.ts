import { Component, OnInit } from '@angular/core';
import {HealthdataService} from '../service/healthdata.service';
import {HealthData} from '../model/healthdata';

@Component({
  selector: 'app-healthdata',
  templateUrl: './healthdata.component.html',
  styleUrls: ['./healthdata.component.css']
})
export class HealthdataComponent implements OnInit {
  healthData = [];

  constructor(private healthdataService: HealthdataService) { }

  ngOnInit() {
    this.healthdataService.healthData.subscribe(res => this.healthData = res);
    this.healthdataService.changeHealthData(this.healthData);
  }

  addHealthData() {
    //this.healthData.push(new HealthData());
    this.healthdataService.changeHealthData(this.healthData);
  }
}
