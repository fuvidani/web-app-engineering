import {Injectable} from '@angular/core';
import {BehaviorSubject} from 'rxjs/BehaviorSubject';
import {HealthData} from '../model/healthdata';
import {HealthDataQuery} from '../model/healthdataquery';
import {HealthDataShare} from '../model/healthdatashare';

@Injectable()
export class HealthdataService {

  // TODO change this to backend call
  private data1 = new HealthData('Meine Zähne', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.', 'https://upload.wikimedia.org/wikipedia/commons/thumb/6/62/Tired_teeth.jpg/220px-Tired_teeth.jpg', ['tooth', 'shit']);
  private data2 = new HealthData('Brust', 'Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.', 'http://st1.thehealthsite.com/wp-content/uploads/2017/04/Worst-things-that-can-happen-to-your-breasts.jpg', ['breast', 'titties', 'bh']);
  private data3 = new HealthData('Brust', '', 'http://st1.thehealthsite.com/wp-content/uploads/2017/04/Worst-things-that-can-happen-to-your-breasts.jpg', ['breast', 'titties', 'bh']);
  private data4 = new HealthData('Brust', 'Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.', '', ['breast', 'titties', 'bh']);
  private healthDataPlaceholder = new BehaviorSubject<any>([this.data1, this.data2, this.data3, this.data4]);
  healthData = this.healthDataPlaceholder.asObservable();

  private sharedata1 = new HealthDataShare('1', 'Meine Zähne');
  private sharedata2 = new HealthDataShare('2', 'Mein Körper');
  private sharedata3 = new HealthDataShare('3', 'Beine');
  private sharedata4 = new HealthDataShare('4', 'Bla bla');
  private sharedata5 = new HealthDataShare('5', 'Penis');
  private query1 = new HealthDataQuery('1', 'query1', 'description of query 1', 'institute1', 10, [this.sharedata1, this.sharedata2, this.sharedata3]);
  private query2 = new HealthDataQuery('2', 'query2', 'description of query 2', 'institute2', 20, [this.sharedata4, this.sharedata5]);
  private healthDataQueriesPlaceholder = new BehaviorSubject<any>([this.query1, this.query2]);
  healthDataQueries = this.healthDataQueriesPlaceholder.asObservable();

  constructor() {
  }

  changeHealthData(healthData) {
    this.healthDataPlaceholder.next(healthData);
  }

  shareHealthData(sharedData) {
    console.log(sharedData);
  }
}
