import { TestBed, inject } from '@angular/core/testing';

import { MedicalqueryService } from './medicalquery.service';

describe('MedicalqueryService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [MedicalqueryService]
    });
  });

  it('should be created', inject([MedicalqueryService], (service: MedicalqueryService) => {
    expect(service).toBeTruthy();
  }));
});
