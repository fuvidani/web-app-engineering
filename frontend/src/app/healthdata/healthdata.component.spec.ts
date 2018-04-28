import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { HealthdataComponent } from './healthdata.component';

describe('HealthdataComponent', () => {
  let component: HealthdataComponent;
  let fixture: ComponentFixture<HealthdataComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ HealthdataComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(HealthdataComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
