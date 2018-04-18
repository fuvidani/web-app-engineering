import { TestBed, inject } from '@angular/core/testing';

import { HealthdataService } from './healthdata.service';

describe('HealthdataService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [HealthdataService]
    });
  });

  it('should be created', inject([HealthdataService], (service: HealthdataService) => {
    expect(service).toBeTruthy();
  }));
});
