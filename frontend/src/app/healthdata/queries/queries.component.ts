import {Component, OnInit} from '@angular/core';
import {AuthService} from '../../service/auth.service';
import {HealthdataService} from '../../service/healthdata.service';
import {Router} from '@angular/router';
import {MatTableDataSource} from '@angular/material';
import {SelectionModel} from '@angular/cdk/collections';
import {HealthDataQuery} from '../../model/healthdataquery';
import {HealthDataShare} from '../../model/healthdatashare';

@Component({
  selector: 'app-queries',
  templateUrl: './queries.component.html',
  styleUrls: ['./queries.component.css']
})
export class QueriesComponent implements OnInit {
  email = '';
  queries = [];
  displayedColumns = null;
  dataSource = null;
  selections = [];

  constructor(private healthdataService: HealthdataService, private authService: AuthService, private router: Router) {
  }

  ngOnInit() {
    this.healthdataService.fetchHelthDataQueries().subscribe(response => {
        const responseObject = JSON.parse(response);
        // TODO parse dynamically
        const data = new HealthDataQuery(responseObject.queryId, responseObject.queryName, responseObject.queryDescription, responseObject.queryInstituteName, responseObject.queryPrice, responseObject.medicalInfo);
        this.queries.push(data);
        this.dataSource = new MatTableDataSource<HealthDataQuery>(this.queries);
        this.dataSource.data.forEach(source => this.selections.push({
          'id': source.id,
          selection: new SelectionModel<HealthDataShare>(true, [])
        }));
        // dirty hack to update view
        document.getElementById('trickButton').click();
      },
      err => console.error(err),
      () => console.log('done loading queries')
    );

    this.email = this.authService.getPrincipal().email;
    // this.healthdataService.healthDataQueries.subscribe(res => this.queries = res);
    this.displayedColumns = ['select', 'id', 'name'];
    this.dataSource = new MatTableDataSource<HealthDataQuery>(this.queries);
    this.dataSource.data.forEach(source => this.selections.push({
      'id': source.id,
      selection: new SelectionModel<HealthDataShare>(true, [])
    }));
  }

  logOut() {
    this.authService.handleLogout(this.router)
  }

  home() {
    const promise = this.router.navigate(['/healthdata']);
  }

  isAllSelected(data) {
    const numSelected = this.getSelection(data).selected.length;
    const numRows = data.healthData.length;
    return numSelected === numRows;
  }

  masterToggle(data) {
    this.isAllSelected(data) ?
      this.getSelection(data).clear() :
      data.healthData.forEach(row => this.getSelection(data).select(row));
  }

  getSelection(data) {
    return this.selections.find(selection => selection.id === data.id).selection;
  }

  share(data) {
    this.healthdataService.shareHealthData(this.selections.find(selection => selection.id === data.id)).subscribe(
      res => {
        console.log(res);

        // remove query from list
        this.queries = this.queries.filter(function (query) {
          return query.id !== data.id;
        });
        this.dataSource = new MatTableDataSource<HealthDataQuery>(this.queries);
        this.dataSource.data.forEach(source => this.selections.push({
          'id': source.id,
          selection: new SelectionModel<HealthDataShare>(true, [])
        }));
      },
      err => console.error(err)
    );
  }

  scroll(element) {
    element.scrollIntoView({behavior: 'smooth'});
  }
}
