/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { JugadorService } from './jugador.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';

describe('Service: Jugador', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [JugadorService]
    });
  });

  it('should ...', inject([JugadorService], (service: JugadorService) => {
    expect(service).toBeTruthy();
  }));
});
