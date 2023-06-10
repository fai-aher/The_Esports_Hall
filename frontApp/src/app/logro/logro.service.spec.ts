/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { LogroService } from './logro.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';

describe('Service: Jugador', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [LogroService]
    });
  });

  it('should ...', inject([LogroService], (service: LogroService) => {
    expect(service).toBeTruthy();
  }));
});
