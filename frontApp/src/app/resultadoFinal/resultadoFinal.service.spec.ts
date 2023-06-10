/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { ResultadoFinalService } from './resultadoFinal.service';
import { HttpClientModule } from '@angular/common/http';

describe('Service: ResultadoFinal', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientModule],
      providers: [ResultadoFinalService]
    });
  });

  it('should ...', inject([ResultadoFinalService], (service: ResultadoFinalService) => {
    expect(service).toBeTruthy();
  }));
});
