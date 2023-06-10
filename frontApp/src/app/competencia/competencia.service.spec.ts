/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { CompetenciaService } from './competencia.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';

describe('Service: Competencia', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [CompetenciaService]
    });
  });

  it('should ...', inject([CompetenciaService], (service: CompetenciaService) => {
    expect(service).toBeTruthy();
  }));
});
