import { TestBed, inject } from '@angular/core/testing';

import { BrowsersupportService } from './browsersupport.service';

describe('BrowsersupportService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [BrowsersupportService]
    });
  });

  it('should be created', inject([BrowsersupportService], (service: BrowsersupportService) => {
    expect(service).toBeTruthy();
  }));
});
