import {Component, OnInit} from '@angular/core';
import {AuthService} from '../service/auth.service';
import {Router} from '@angular/router';
import {MedicalQuery} from '../model/medicalquery';
import {MedicalqueryService} from '../service/medicalquery.service';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {MatChipInputEvent} from "@angular/material";
import {COMMA, ENTER} from "@angular/cdk/keycodes";
import {Gender} from "../model/gender";

@Component({
  selector: 'app-medicalquery',
  templateUrl: './medicalquery.component.html',
  styleUrls: ['./medicalquery.component.css']
})
export class MedicalqueryComponent implements OnInit {

  email: string;
  test = new MedicalQuery(2, 'TU Vienna', 'Abfrage 1', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.', 10, 50, 60, 'FEMALE', ['cancer', 'skin']);
  genders = [
    new Gender('1', 'Male', 'MALE'),
    new Gender('2', 'Female', 'FEMALE')
  ];


  medicalQueryForm: FormGroup;
  name: FormControl;
  description: FormControl;
  financialOffering: FormControl;
  gender: FormControl;
  tags: FormControl;

  tagList;
  separatorKeysCodes = [ENTER, COMMA];

  constructor(
    private authService: AuthService,
    private medicalQueryService: MedicalqueryService,
    private router: Router
  ) {
    this.name = new FormControl(
      '',
      [Validators.required]
    );
    this.description = new FormControl(
      '',
      [Validators.required]);
    this.financialOffering = new FormControl(
      '',
      [Validators.required, Validators.min(0)]);
    this.gender = new FormControl(
      '',
      [Validators.required]
    );
    /*this.tags = new FormControl(
      '',
      [Validators.required]);*/
    this.medicalQueryForm = new FormGroup({
      name: this.name,
      description: this.description,
      financialOffering: this.financialOffering,
      //tags: this.tags,
      gender: this.gender
    });
  }

  ngOnInit() {
    this.email = this.authService.getPrincipal().email;
    this.tagList = [];
  }

  scroll() {
    //elememt.scrollIntoView({behavior:'smooth'});
    this.medicalQueryService.addMedicalQuery(this.test);
  }

  logOut() {
    this.authService.clearAccessToken();
    this.router.navigate(['/']);
  }

  navigateToSharedHealthdata(qid: string) {
    this.router.navigate(['medicalquery', qid, 'shared']);
  }

  onSubmit() {
    if (this.medicalQueryForm.valid) {
      console.log("Medicalquery form is valid. Can be submitted.")
      console.log(this.medicalQueryForm.value);
    }
  }

  addTag(event: MatChipInputEvent): void {
    const input = event.input;
    const value = event.value;

    if ((value || '').trim()) {
      this.tagList.push({name: value.trim()});
    }

    if (input) {
      input.value = '';
    }
  }

  removeTag(tag: string): void {
    const index = this.tagList.indexOf(tag);

    if (index >= 0) {
      this.tagList.splice(index, 1);
    }
  }

}
