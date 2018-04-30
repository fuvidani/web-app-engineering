import {Injectable} from '@angular/core';
import {MedicalQuery} from '../model/medicalquery';
import {Observable} from 'rxjs/Observable';
import {HttpClient} from '@angular/common/http';
import * as EventSource from 'eventsource';
import {AuthService} from './auth.service';

@Injectable()
export class MedicalqueryService {

  constructor(private http: HttpClient, private authService: AuthService) {
  }

  fetchMedicalQueries() {
    console.log('Loading initial medical queries from backend.');
    const url = String('http://localhost:8080/user/')
      .concat(this.authService.getPrincipal().sub + '/medicalQuery');

    return Observable.create(observer => {
      const options = {
        headers: {
          Authorization: 'Bearer ' + localStorage.getItem('access_token')
        }
      };

      const eventSource = new EventSource(url, options);
      eventSource.onmessage = x => observer.next(x.data);
      eventSource.onerror = x => observer.error(x);

      return () => {
        eventSource.close();
      };
    });
  }

  addMedicalQuery(newMedicalQuery: MedicalQuery) {
    const url = String('http://localhost:8080/user/')
    .concat(newMedicalQuery.researchFacilityId + '/medicalQuery');

    return this.http.post<MedicalQuery>(url, newMedicalQuery);
  }

  fetchSharedHealthDataForQuery(qid) {
    return Observable.create(observer => {
      const options = {
        headers: {
          Authorization: 'Bearer ' + localStorage.getItem('access_token')
        }
      };

      const eventSource = new EventSource('http://localhost:8080/user/' + this.authService.getPrincipal().sub + '/medicalQuery/' + qid + '/shared', options);
      eventSource.onmessage = x => observer.next(x.data);
      eventSource.onerror = x => observer.error(x);

      return () => {
        eventSource.close();
      };
    });
  }

}
