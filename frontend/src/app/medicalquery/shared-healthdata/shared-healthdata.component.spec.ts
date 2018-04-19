import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SharedHealthdataComponent } from './shared-healthdata.component';

describe('SharedHealthdataComponent', () => {
  let component: SharedHealthdataComponent;
  let fixture: ComponentFixture<SharedHealthdataComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SharedHealthdataComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SharedHealthdataComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
