/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { asNativeElements, DebugElement } from '@angular/core';
import { faker } from '@faker-js/faker';

import { JugadorListComponent } from './jugador-list.component';
import { HttpClientModule } from '@angular/common/http';
import { JugadorService } from '../jugador.service';
import { JugadorDetail } from '../jugador-detail';
import { RouterTestingModule } from '@angular/router/testing';
import { TorneoDetail } from 'src/app/torneo/torneo-detail';
import { CompetenciaDetail } from 'src/app/competencia/competencia-detail';
import { LogroDetail } from 'src/app/logro/logro-detail';
import { Competencia } from 'src/app/competencia/competencia';
import { Equipo } from 'src/app/equipo/equipo';
import { EquipoDetail } from 'src/app/equipo/equipo-detail';
import { Jugador } from '../jugador';

describe('JugadorListComponent', () => {
  let component: JugadorListComponent;
  let fixture: ComponentFixture<JugadorListComponent>;
  let debug: DebugElement;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports:[HttpClientModule, RouterTestingModule],
      declarations: [ JugadorListComponent ],
    })
    .compileComponents();
  }));

  /*

  beforeEach(() => {
    fixture = TestBed.createComponent(JugadorListComponent);
    component = fixture.componentInstance;

    let testJugadores: Array<JugadorDetail> = [];
    let testTorneos: Array<TorneoDetail> = [];
    let testCompetencias: Array<CompetenciaDetail> = [];
    let testLogros: Array<LogroDetail> = [];


    for(let i = 0; i<2; i++){
      testTorneos[i] = new TorneoDetail(
        faker.datatype.number(),
        faker.lorem.sentence(),
        faker.date.past(),
        faker.lorem.sentence(),
        faker.image.imageUrl(),
        faker.lorem.sentence(),
        faker.lorem.sentence(),
        faker.lorem.sentence(),
        [],[]
      );
    }

    component.jugadores = testJugadores;
    fixture.detectChanges();
    debug = fixture.debugElement;
  });

it('should create', () => {
  expect(component).toBeTruthy();
});

it('should have 10 <div.col.mb-2> elements', () => {
  expect(debug.queryAll(By.css('div.col.mb-2'))).toHaveSize(10)
});

it('should have 10 <card.p-2> elements', () => {
  expect(debug.queryAll(By.css('div.card.p-2'))).toHaveSize(10)
});

it('should have 10 <img> elements', () => {
  expect(debug.queryAll(By.css('img'))).toHaveSize(10)
});

it('should have 10 <div.card-body> elements', () => {
  expect(debug.queryAll(By.css('div.card-body'))).toHaveSize(10)
});

it('should have the corresponding src to the jugador fotografia and alt to the jugador nickname', () => {
  debug.queryAll(By.css('img')).forEach((img, i)=>{
    expect(img.attributes['src']).toEqual(
      component.jugadores[i].fotografia)

    expect(img.attributes['alt']).toEqual(
      component.jugadores[i].nickname)
  })
});

it('should have h5 tag with the jugador.nombre', () => {
  debug.queryAll(By.css('h5.card-title')).forEach((h5, i)=>{
    expect(h5.nativeElement.textContent).toContain(component.jugadores[i].nombre)
  });
});


it('should have 9 <div.col.mb-2> elements and the deleted jugador should not exist', () => {
  const jugador = component.jugadores.pop()!;
  fixture.detectChanges();
  expect(debug.queryAll(By.css('div.col.mb-2'))).toHaveSize(9)

  debug.queryAll(By.css('div.col.mb-2')).forEach((selector, i)=>{
    expect(selector.nativeElement.textContent).not.toContain(jugador.nombre);
  });
});

*/

});
