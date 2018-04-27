import {Injectable} from '@angular/core';
import {MedicalQuery} from '../model/medicalquery';
import {BehaviorSubject} from 'rxjs/BehaviorSubject';
import {Observable} from 'rxjs/Observable';
import {HttpClient} from '@angular/common/http';

@Injectable()
export class MedicalqueryService {

  dummyQuery1: MedicalQuery;
  dummyQuery2: MedicalQuery;

  private medicalQueryStore: MedicalQuery[];
  private _medicalQueries: BehaviorSubject<MedicalQuery[]>;
  medicalQueries: Observable<MedicalQuery[]>;

  constructor(private http: HttpClient) {
    this.loadInitialData();
    this.dummyQuery1 = new MedicalQuery(1, 'StarLabs', 'Abfrage 1', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.', 5, 18, 25, 'MALE', ['cancer', 'brain']);
    this.dummyQuery2 = new MedicalQuery(2, 'TU Vienna', 'Abfrage 1', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.', 10, 50, 60, 'FEMALE', ['cancer', 'skin']);
    this.medicalQueryStore = [this.dummyQuery1, this.dummyQuery2];
    this._medicalQueries = new BehaviorSubject<MedicalQuery[]>(this.medicalQueryStore);
    this.medicalQueries = this._medicalQueries.asObservable();

  }

  loadInitialData() {
    console.log('Loading initial medical queries from backend.');
    //TODO: load data from backend
  }

  addMedicalQuery(newMedicalQuery: MedicalQuery) {
    //TODO: Call backend and save query
    this.medicalQueryStore.push(newMedicalQuery);
    this._medicalQueries.next(this.medicalQueryStore);
  }

}
