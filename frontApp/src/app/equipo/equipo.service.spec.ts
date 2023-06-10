/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { EquipoService } from './equipo.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';

describe('Service: Equipo', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [EquipoService]
    });
  });

  it('should ...', inject([EquipoService], (service: EquipoService) => {
    expect(service).toBeTruthy();
  }));
});
