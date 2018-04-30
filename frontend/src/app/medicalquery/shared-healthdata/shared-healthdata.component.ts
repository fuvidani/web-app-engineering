import {Component, OnInit} from '@angular/core';
import {HealthData} from '../../model/healthdata';
import {AuthService} from '../../service/auth.service';
import {ActivatedRoute, Router} from '@angular/router';
import {MedicalqueryService} from '../../service/medicalquery.service';
import {AnonymizedSharedData} from '../../model/anonymizedshareddata';

@Component({
  selector: 'app-shared-healthdata',
  templateUrl: './shared-healthdata.component.html',
  styleUrls: ['./shared-healthdata.component.css']
})
export class SharedHealthdataComponent implements OnInit {

  email: string;
  qid: string;

  // TODO make server call instead
  data1 = new HealthData('1', 'Brust', 'Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.', 'http://st1.thehealthsite.com/wp-content/uploads/2017/04/Worst-things-that-can-happen-to-your-breasts.jpg', ['breast', 'titties', 'bh'], '');
  data2 = new HealthData('2', 'asd', 'Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.', 'https://i.ytimg.com/vi/rhRAF91_7jI/maxresdefault.jpg', ['asd', 'fgdg fdg'], '');
  sharedData = [new AnonymizedSharedData('1', 'userID1', '1993.01.02.', 'MALE', [this.data1, this.data2]), new AnonymizedSharedData('2', 'userID2', '1999.12.12.', 'FEMALE', [this.data2])];
  // sharedData = [];

  constructor(
    private authService: AuthService,
    private router: Router,
    private route: ActivatedRoute,
    private medicalqueryService: MedicalqueryService
  ) {
  }

  ngOnInit() {
    this.email = this.authService.getPrincipal().email;
    this.route.params.subscribe(params => this.qid = params['qid']);

    this.medicalqueryService.fetchSharedHealthDataForQuery(this.qid).subscribe(response => {
        const responseObject = JSON.parse(response);
        // TODO parse dynamically
        const data = new AnonymizedSharedData(responseObject.id, responseObject.userId, responseObject.birthday, responseObject.gender, responseObject.medicalInformation);

        // TODO push data to array
        // this.sharedData.push(data);

        // dirty hack to update view
        // document.getElementById('trickButton').click();
      },
      err => console.error(err),
      () => console.log('done loading health data')
    );
  }

  logOut() {
    this.authService.clearAccessToken();
    const promise = this.router.navigate(['/']);
  }

  home() {
    const promise = this.router.navigate(['/medicalquery']);
  }

  scroll(element) {
    element.scrollIntoView({behavior: 'smooth'});
  }
}
