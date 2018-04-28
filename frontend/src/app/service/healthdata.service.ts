import {Injectable} from '@angular/core';
import {BehaviorSubject} from 'rxjs/BehaviorSubject';
import {HealthData} from '../model/healthdata';
import {HealthDataQuery} from '../model/healthdataquery';
import {HealthDataShare} from '../model/healthdatashare';
import {AuthService} from './auth.service';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs/Observable';
import * as EventSource from 'eventsource';
import {Subject} from 'rxjs/Subject';

@Injectable()
export class HealthdataService {

  private sharedata1 = new HealthDataShare('1', 'Meine Zähne');
  private sharedata2 = new HealthDataShare('2', 'Mein Körper');
  private sharedata3 = new HealthDataShare('3', 'Beine');
  private sharedata4 = new HealthDataShare('4', 'Bla bla');
  private sharedata5 = new HealthDataShare('5', 'Penis');
  private query1 = new HealthDataQuery('1', 'query1', 'description of query 1', 'institute1', 10, [this.sharedata1, this.sharedata2, this.sharedata3]);
  private query2 = new HealthDataQuery('2', 'query2', 'description of query 2', 'institute2', 20, [this.sharedata4, this.sharedata5]);
  private healthDataQueriesPlaceholder = new BehaviorSubject<any>([this.query1, this.query2]);
  healthDataQueries = this.healthDataQueriesPlaceholder.asObservable();

  healthData = new Array<HealthData>();
  healthDataSubject = new Subject();
  healthDataList$ = this.healthDataSubject.asObservable();

  constructor(private authService: AuthService, private http: HttpClient) {
    this.notify();
  }

  // insertNew():void {
  //   // Here we are updating the API
  //   this.http.post(this.root + "/users", {
  //     name: "This is my new one"
  //   }).subscribe((res) => {
  //     // The API returns our newly created item, so append it to data, and
  //     // call notify again to update the observable
  //     this.data.push(res);
  //     this.notify();
  //   })

  // }

  private notify() {
    // Call next on the subject with the latest data
    this.healthDataSubject.next(this.healthData);
  }

  getHealthDataList() {
    this.fetchHeathData().subscribe(response => {
        const responseObject = JSON.parse(response);
        // TODO parse dynamically
        const data = new HealthData(responseObject.id, responseObject.title, responseObject.description, responseObject.image, responseObject.tags, responseObject.userId);
        // console.log(data);

        this.healthData.push(data);
        this.notify();
      },
      err => console.error(err),
      () => console.log('done loading health data')
    );
  }

  fetchHeathData() {
    return Observable.create(observer => {
      const options = {
        headers: {
          Authorization: 'Bearer ' + localStorage.getItem('access_token')
        }
      };

      const eventSource = new EventSource('http://localhost:8080/user/' + this.authService.getPrincipal().sub + '/medicalInformation', options);
      eventSource.onmessage = x => observer.next(x.data);
      eventSource.onerror = x => observer.error(x);

      return () => {
        eventSource.close();
      };
    });
  }

  uploadHealthData(data) {
    data.userId = this.authService.getPrincipal().sub;
    return this.http.post<HealthData>('http://localhost:8080/user/' + this.authService.getPrincipal().sub + '/medicalInformation', data);
  }

  shareHealthData(sharedData) {
    console.log(sharedData);
  }
}
