import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MedicalqueryComponent } from './medicalquery.component';

describe('MedicalqueryComponent', () => {
  let component: MedicalqueryComponent;
  let fixture: ComponentFixture<MedicalqueryComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MedicalqueryComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MedicalqueryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
