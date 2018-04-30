import {Injectable} from '@angular/core';
import {HealthData} from '../model/healthdata';
import {AuthService} from './auth.service';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs/Observable';
import * as EventSource from 'eventsource';
import {SharingPermission} from '../model/sharingpermission';

@Injectable()
export class HealthdataService {

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

      const eventSource = new EventSource('http://localhost:8080/user/' + this.authService.getPrincipal().sub + '/medicalQuery/matching', options);
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
    sharedData.selection.selected.forEach(healthData => permissions.push(new SharingPermission(null, healthData.first, sharedData.id)));

    return this.http.post<Array<SharingPermission>>('http://localhost:8080/user/' + this.authService.getPrincipal().sub + '/medicalQuery/permissions', permissions);
  }
}
