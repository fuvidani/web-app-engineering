import {Injectable} from '@angular/core';
import {BehaviorSubject} from 'rxjs/BehaviorSubject';
import {HealthData} from '../model/healthdata';
import {HealthDataQuery} from '../model/healthdataquery';
import {HealthDataShare} from '../model/healthdatashare';
import {AuthService} from './auth.service';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs/Observable';
import * as EventSource from 'eventsource';
import {SharingPermission} from '../model/sharingpermission';

@Injectable()
export class HealthdataService {

  // TODO change this placeholder to real server request
  private sharedata1 = new HealthDataShare('1', 'Meine Zähne');
  private sharedata2 = new HealthDataShare('2', 'Mein Körper');
  private sharedata3 = new HealthDataShare('3', 'Beine');
  private sharedata4 = new HealthDataShare('4', 'Bla bla');
  private sharedata5 = new HealthDataShare('5', 'Penis');
  private query1 = new HealthDataQuery('1', 'query1', 'description of query 1', 'institute1', 10, [this.sharedata1, this.sharedata2, this.sharedata3]);
  private query2 = new HealthDataQuery('2', 'query2', 'description of query 2', 'institute2', 20, [this.sharedata4, this.sharedata5]);
  private healthDataQueriesPlaceholder = new BehaviorSubject<any>([this.query1, this.query2]);
  healthDataQueries = this.healthDataQueriesPlaceholder.asObservable();

  constructor(private authService: AuthService, private http: HttpClient) {
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

  fetchHelthDataQueries() {
    return Observable.create(observer => {
      const options = {
        headers: {
          Authorization: 'Bearer ' + localStorage.getItem('access_token')
        }
      };

      const eventSource = new EventSource('http://localhost:8080/user/' + this.authService.getPrincipal().sub + '/medicalInformation/matching', options);
      eventSource.onmessage = x => observer.next(x.data);
      eventSource.onerror = x => observer.error(x);

      return () => {
        eventSource.close();
      };
    });
  }

  uploadHealthData(data) {
    // append userId into object
    data.userId = this.authService.getPrincipal().sub;
    return this.http.post<HealthData>('http://localhost:8080/user/' + this.authService.getPrincipal().sub + '/medicalInformation', data);
  }

  shareHealthData(sharedData) {
    const permissions = [];
    sharedData.selection.selected.forEach(healthData => permissions.push(new SharingPermission('', healthData.id, sharedData.id)));

    return this.http.post<Array<SharingPermission>>('http://localhost:8080/user/' + this.authService.getPrincipal().sub + '/medicalQuery/permissions', permissions);
  }
}
