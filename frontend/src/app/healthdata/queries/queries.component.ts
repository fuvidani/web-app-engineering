import { Component, OnInit } from '@angular/core';
import {AuthService} from '../../service/auth.service';
import {HealthdataService} from '../../service/healthdata.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-queries',
  templateUrl: './queries.component.html',
  styleUrls: ['./queries.component.css']
})
export class QueriesComponent implements OnInit {
  email = '';

  constructor(private healthdataService: HealthdataService, private authService: AuthService, private router: Router) { }

  ngOnInit() {
    this.email = this.authService.getPrincipal();
  }

  logOut() {
    this.authService.clearAccessToken();
    this.router.navigate(['/']);
  }

}
