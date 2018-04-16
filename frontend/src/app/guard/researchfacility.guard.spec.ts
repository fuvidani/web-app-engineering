import { TestBed, async, inject } from '@angular/core/testing';

import { ResearchfacilityGuard } from './researchfacility.guard';

describe('ResearchfacilityGuard', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [ResearchfacilityGuard]
    });
  });

  it('should ...', inject([ResearchfacilityGuard], (guard: ResearchfacilityGuard) => {
    expect(guard).toBeTruthy();
  }));
});
