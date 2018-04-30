import {Component, OnInit, ViewChild} from '@angular/core';
import {HealthdataService} from '../service/healthdata.service';
import {HealthData} from '../model/healthdata';
import {AuthService} from '../service/auth.service';
import {Router} from '@angular/router';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {ENTER} from '@angular/cdk/keycodes';
import {MatChipInputEvent, MatTableDataSource} from '@angular/material';
import {HealthDataShare} from '../model/healthdatashare';
import {SelectionModel} from '@angular/cdk/collections';
import {HealthDataQuery} from '../model/healthdataquery';

@Component({
  selector: 'app-healthdata',
  templateUrl: './healthdata.component.html',
  styleUrls: ['./healthdata.component.css']
})
export class HealthdataComponent implements OnInit {
  healthData = [];
  queries = [];
  email = '';

  error = false;
  errorText = '';

  // workaround for known bug (form validation not cleared after form reset)
  @ViewChild('f') myForm;

  uploadForm: FormGroup;
  title: FormControl;
  description: FormControl;
  imageBase64: '';

  separatorKeysCodes = [ENTER];

  tags = [];
  fileToUpload: File = null;


  constructor(private healthdataService: HealthdataService, private authService: AuthService, private router: Router) {
  }

  ngOnInit() {
    this.healthdataService.fetchHeathData().subscribe(response => {
        const responseObject = JSON.parse(response);
        // TODO parse dynamically
        const data = new HealthData(responseObject.id, responseObject.title, responseObject.description, responseObject.image, responseObject.tags, responseObject.userId);

        this.healthData.push(data);
        // dirty hack to update view
        document.getElementById('trickButton').click();
      },
      err => console.error(err),
      () => console.log('done loading health data')
    );

    this.healthdataService.fetchHelthDataQueries().subscribe(response => {
        const responseObject = JSON.parse(response);
        // TODO parse dynamically
        const data = new HealthDataQuery(responseObject.queryId, responseObject.queryName, responseObject.queryDescription, responseObject.queryInstituteName, responseObject.queryPrice, responseObject.medicalInfo);
        this.queries.push(data);

        // dirty hack to update view
        document.getElementById('trickButton').click();
      },
      err => console.error(err),
      () => console.log('done loading queries')
    );

    this.email = this.authService.getPrincipal().email;

    this.title = new FormControl(
      '',
      [Validators.required]
    );

    this.description = new FormControl(
      '',
      []
    );

    this.uploadForm = new FormGroup({
      title: this.title,
      description: this.description,
    });
  }

  addHealthData() {
    const tagArray = [];
    this.tags.forEach(tag => tagArray.push(tag.name));
    const data = new HealthData(null, this.title.value, this.description.value, this.imageBase64, tagArray, null);

    this.healthdataService.uploadHealthData(data).subscribe(response => {
        this.healthData.push(response);
      },
      err => console.error(err),
      () => console.log('post new health data ended')
    );
  }

  logOut() {
    this.authService.clearAccessToken();
    const promise = this.router.navigate(['/']);
  }

  navigateToQueries() {
    const promise = this.router.navigate(['/userqueries']);
  }

  scroll(element) {
    element.scrollIntoView({behavior: 'smooth'});
  }

  onSubmit() {
    if (this.title.value === '') {
      this.error = true;
      this.errorText = 'Title has to be present';
    } else if (this.tags.length < 1) {
      this.error = true;
      this.errorText = 'At least one tag has to be present';
    } else if (this.fileToUpload === null && this.description.value === '') {
      this.error = true;
      this.errorText = 'Description or image has to be present';
    } else {
      this.error = false;
      if (this.fileToUpload !== null) {
        this.convertToBase64(this.fileToUpload);
      } else {
        this.addHealthData();
        this.myForm.resetForm();
        this.tags = [];
      }
    }
  }

  handleFileInput(files: FileList) {
    this.fileToUpload = files.item(0);
  }

  convertToBase64(file: File): void {
    const myReader: FileReader = new FileReader();

    myReader.onloadend = (e) => {
      this.imageBase64 = myReader.result;
      this.addHealthData();
      this.myForm.resetForm();
      this.tags = [];
      this.fileToUpload = null;
      this.imageBase64 = '';
    };

    myReader.readAsDataURL(file);
  }

  openInput() {
    document.getElementById('fileInput').click();
  }

  add(event: MatChipInputEvent): void {
    const input = event.input;
    const value = event.value;

    if ((value || '').trim()) {
      this.tags.push({name: value.trim()});
    }

    if (input) {
      input.value = '';
    }
  }

  remove(tag: any): void {
    const index = this.tags.indexOf(tag);

    if (index >= 0) {
      this.tags.splice(index, 1);
    }
  }
}
