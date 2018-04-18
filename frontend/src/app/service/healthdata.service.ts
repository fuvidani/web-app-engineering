import { Injectable } from '@angular/core';
import {BehaviorSubject} from 'rxjs/BehaviorSubject';
import {HealthData} from '../model/healthdata';

@Injectable()
export class HealthdataService {

  // TODO change this to backend call
  private data1 = new HealthData('Meine Zähne', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.', 'https://upload.wikimedia.org/wikipedia/commons/thumb/6/62/Tired_teeth.jpg/220px-Tired_teeth.jpg', ['tooth', 'shit']);
  private data2 = new HealthData('Brust', 'Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.', 'http://st1.thehealthsite.com/wp-content/uploads/2017/04/Worst-things-that-can-happen-to-your-breasts.jpg', ['breast', 'titties', 'bh']);
  private healthDataPlaceholder = new BehaviorSubject<any>([this.data1, this.data2]);
  healthData = this.healthDataPlaceholder.asObservable();

  constructor() { }

  changeHealthData(healthData) {
    this.healthDataPlaceholder.next(healthData);
  }
}
