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
  genders = [
    new Gender('1', 'Male', 'MALE'),
    new Gender('2', 'Female', 'FEMALE')
  ];

  medicalQueryForm: FormGroup;

  name: FormControl;
  description: FormControl;
  financialOffering: FormControl;
  gender: FormControl;
  minAge: FormControl;
  maxAge: FormControl;
  tags: FormControl;

  tagList;
  separatorKeysCodes = [ENTER, COMMA];

  error: boolean;
  errorText: string;

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
    this.gender = new FormControl('');
    this.minAge = new FormControl('');
    this.maxAge = new FormControl('');
    this.medicalQueryForm = new FormGroup({
      name: this.name,
      description: this.description,
      financialOffering: this.financialOffering,
      gender: this.gender,
      minAge: this.minAge,
      maxAge: this.maxAge
    });
  }

  ngOnInit() {
    this.email = this.authService.getPrincipal().email;
    this.error = false;
    this.errorText = '';
    this.tagList = [];
    this.medicalQueryService.loadInitialData();
  }

  scroll() {
    //elememt.scrollIntoView({behavior:'smooth'});
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
      let medicalQuery: MedicalQuery = this.medicalQueryForm.value;
      medicalQuery.researchFacility = this.authService.getPrincipal().sub;
      medicalQuery.tags = this.parseTagList(this.tagList);

      if(this.atLeastOneCriteria(medicalQuery)){
        this.error = true;
        this.errorText = 'You must provide at least one criteria.';
        return;
      }

      if(medicalQuery.gender == ''){
        //Backend does not accept an empty String for an optional property
        medicalQuery.gender = null;
      }

      if(medicalQuery.minAge > medicalQuery.maxAge) {
        this.error = true;
        this.errorText = 'Max. age must be greater or equal to min. age.';
        return;
      }

      this.medicalQueryService.addMedicalQuery(medicalQuery);
      this.error = false;
      this.errorText = '';
    }
  }

  atLeastOneCriteria(medicalQuery: MedicalQuery): boolean {
    return !(medicalQuery.tags.length > 0) &&
      (!medicalQuery.minAge || !medicalQuery.maxAge) &&
      (medicalQuery.gender == '');
  }

  parseTagList(tagList: any[]): string[] {
    let tagArray = [];
    this.tagList.forEach(tag => tagArray.push(tag.name));
    return tagArray;
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
