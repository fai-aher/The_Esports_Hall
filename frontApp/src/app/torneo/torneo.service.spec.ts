/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { TorneoService } from './torneo.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';

describe('Service: Torneo', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [TorneoService]
    });
  });

  it('should ...', inject([TorneoService], (service: TorneoService) => {
    expect(service).toBeTruthy();
  }));
});
