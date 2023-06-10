/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { asNativeElements, DebugElement } from '@angular/core';
import { faker } from '@faker-js/faker';

import { CompetenciaListComponent } from './competencia-list.component';
import { HttpClientModule } from '@angular/common/http';
import { CompetenciaService } from '../competencia.service';
import { CompetenciaDetail } from '../competencia-detail';
import { RouterTestingModule } from '@angular/router/testing';
import {NgxPaginationModule} from 'ngx-pagination';
import { Torneo } from 'src/app/torneo/torneo';
import { Equipo } from 'src/app/equipo/equipo';
import { Jugador } from 'src/app/jugador/jugador';

describe('CompetenciaListComponent', () => {
  let component: CompetenciaListComponent;
  let fixture: ComponentFixture<CompetenciaListComponent>;
  let debug: DebugElement;
  let debugCompetenciaDetail: CompetenciaDetail;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientModule, RouterTestingModule, NgxPaginationModule],
      declarations: [ CompetenciaListComponent ],
      providers: [ CompetenciaService ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CompetenciaListComponent);
    component = fixture.componentInstance;

    const torneo = new Torneo (
      faker.datatype.number(),
      faker.lorem.sentence(),
      faker.lorem.sentence(),
      faker.lorem.sentence(),
      faker.lorem.sentence(),
      faker.lorem.sentence(),
      faker.lorem.sentence(),
      faker.lorem.sentence()
    );

    const equipo = new Equipo (
      faker.datatype.number(),
      faker.lorem.sentence(),
      faker.lorem.sentence(),
      faker.image.imageUrl(),
      faker.image.imageUrl(),
    );

    const jugador = new Jugador (
      faker.datatype.number(),
      faker.lorem.sentence(),
      faker.lorem.sentence(),
      faker.lorem.sentence(),
      faker.image.imageUrl(),
      faker.datatype.datetime()
    );

    const mvp = new Jugador (
      faker.datatype.number(),
      faker.lorem.sentence(),
      faker.lorem.sentence(),
      faker.lorem.sentence(),
      faker.image.imageUrl(),
      faker.datatype.datetime()
    );


    let testCompetencias: Array<CompetenciaDetail> = [];

    for(let i = 0; i<10; i++) {
      testCompetencias[i] = new CompetenciaDetail(
        faker.datatype.number(),
        faker.lorem.sentence(),
        faker.lorem.sentence(),
        equipo,
        jugador,
        mvp,
        torneo,
        [],
        []
      );
    }
    component.competencias = testCompetencias;
    fixture.detectChanges();
    debug = fixture.debugElement;
  });
  /*
  it('should create', () => {
    expect(component).toBeTruthy();
  });
  */

});

