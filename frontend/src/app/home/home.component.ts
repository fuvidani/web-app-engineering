import {Component, OnInit} from '@angular/core';
import {AuthService} from '../service/auth.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  constructor(private authService: AuthService, private router: Router) {
  }

  ngOnInit() {
    if (this.authService.hasRole('ROLE_END_USER')) {
      this.router.navigate(['/healthdata']);
    } else if (this.authService.hasRole('ROLE_RESEARCH')) {
      this.router.navigate(['/medicalquery']);
    } else {
      this.router.navigate(['/']);
    }
  }

}
