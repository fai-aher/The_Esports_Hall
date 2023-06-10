/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

import { JugadorDetailComponent } from './jugador-detail.component';
import { HttpClientModule } from '@angular/common/http';
import { RouterTestingModule } from '@angular/router/testing';
import { faker } from '@faker-js/faker';
import { CompetenciaDetail } from 'src/app/competencia/competencia-detail';
import { EquipoDetail } from 'src/app/equipo/equipo-detail';
import { LogroDetail } from 'src/app/logro/logro-detail';
import { TorneoDetail } from 'src/app/torneo/torneo-detail';
import { JugadorDetail } from '../jugador-detail';
import { JugadorService } from '../jugador.service';
import { Competencia } from 'src/app/competencia/competencia';

describe('JugadorDetailComponent', () => {
  let component: JugadorDetailComponent;
  let fixture: ComponentFixture<JugadorDetailComponent>;
  let debug: DebugElement;
  let debugJugadorDetail: JugadorDetail;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientModule, RouterTestingModule],
      declarations: [ JugadorDetailComponent ],
      providers: [ JugadorService ]
    })
    .compileComponents();
  }));

  /*

  beforeEach(() => {
    fixture = TestBed.createComponent(JugadorDetailComponent);
    component = fixture.componentInstance;

    let testTorneos: Array<TorneoDetail> = [];
    let testCompetencias: Array<CompetenciaDetail> = [];
    let testLogros: Array<LogroDetail> = [];
    // let competencia = new Competencia(
    //   faker.datatype.number(),
    //   faker.lorem.sentence(),
    //   faker.lorem.sentence(),
    // );


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

    // for(let i = 0; i<2; i++){
    //   testCompetencias[i] = new CompetenciaDetail(
    //     faker.datatype.number(),
    //     faker.lorem.sentence(),
    //     faker.lorem.sentence(),
    //     new EquipoDetail(faker.datatype.number(), faker.lorem.sentence(), faker.lorem.sentence(), faker.lorem.sentence(), faker.image.imageUrl(), [], [], [], [], []),
    //     new JugadorDetail(faker.datatype.number(), faker.lorem.sentence(), faker.lorem.sentence(), faker.lorem.sentence(), faker.image.imageUrl(), faker.date.past(), [], competencia, []),
    //     new JugadorDetail(faker.datatype.number(), faker.lorem.sentence(), faker.lorem.sentence(), faker.lorem.sentence(), faker.image.imageUrl(), faker.date.past(), [], competencia, []),
    //     new TorneoDetail(faker.datatype.number(), faker.lorem.sentence(), faker.date.past(), faker.lorem.sentence(), faker.image.imageUrl(), faker.lorem.sentence(), faker.lorem.sentence(), faker.lorem.sentence(), [], []),
    //     [],[]
    //   );
    // }

    // for(let i = 0; i<2; i++){
    //   testLogros[i] = new LogroDetail(
    //     faker.datatype.number(),
    //     faker.lorem.sentence(),
    //     new JugadorDetail(faker.datatype.number(), faker.lorem.sentence(), faker.lorem.sentence(), faker.lorem.sentence(), faker.image.imageUrl(), faker.date.past(), [], competencia, [])
    //   );
    // }

    // component.jugadorDetail = new JugadorDetail(
    //   faker.datatype.number(), faker.lorem.sentence(), faker.lorem.sentence(), faker.lorem.sentence(), faker.image.imageUrl(), faker.date.past(), testTorneos,competencia,testCompetencias,testLogros
    // );

    fixture.detectChanges();
    debug = fixture.debugElement;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should have an img element', () => {
    expect(debug.query(By.css('img')).attributes['alt']).toEqual(
      component.jugadorDetail.nombre
    );
  });

  it('should have an img element with src = jugadorDetail.fotografia', () => {
    expect(debug.query(By.css('img')).attributes['src']).toEqual(
      component.jugadorDetail.fotografia
    );
  });

  it('should have an img element with alt= jugadorDetail.nombre', () => {
    expect(debug.query(By.css('img')).attributes['alt']).toEqual(
      component.jugadorDetail.nombre
    );
  });

  it('should have one dd tag for component.jugadorDetail.nickname', () => {
    const allDt : DebugElement[]= debug.queryAll(By.css('dt'));
    let nodo = allDt.find((value) => {
      return value.nativeElement.textContent == 'Nickname';
    });
    expect(nodo?.nativeElement.nextSibling.textContent).toContain(component.jugadorDetail.nickname);
  });

  it('should have one dd tag for component.jugadorDetail.fechaNacimiento', () => {
    const allDt : DebugElement[]= debug.queryAll(By.css('dt'));
    let nodo = allDt.find((value) => {
      return value.nativeElement.textContent == 'Fecha de Nacimiento';
    });
    expect(nodo?.nativeElement.nextSibling.textContent).toContain(component.jugadorDetail.fechaNacimiento);
  });

  it('should have one dd tag for component.jugadorDetail.nacionalidad', () => {
    const allDt : DebugElement[]= debug.queryAll(By.css('dt'));
    let nodo = allDt.find((value) => {
      return value.nativeElement.textContent == 'Nacionalidad';
    });
    expect(nodo?.nativeElement.nextSibling.textContent).toContain(component.jugadorDetail.nacionalidad);
  });

  /*
  it('should have a tag with component.jugadorDetail.competencia.nombre', () => {
    const componentElement: HTMLElement = debug.query(By.css('p.h3.p-2.author-name')).nativeElement;
    expect(componentElement.textContent).toContain(component.jugadorDetail.competencia.nombre);
  });

  it('should have a tag with component.jugadorDetail.torneosParticipados[i].nombreTorneo', () => {
    for (let i = 0; i < component.jugadorDetail.torneosParticipados.length; i++) {
      const componentElement: HTMLElement = debug.queryAll(By.css('ul > li'))[i].nativeElement;
      expect(componentElement.textContent).toContain(component.jugadorDetail.torneosParticipados[i].nombreTorneo);
    }
  });

  it('should have a tag with component.jugadorDetail.competenciasParticipadas[i].nombre', () => {
    for (let i = 0; i < component.jugadorDetail.competenciasParticipadas.length; i++) {
      const componentElement: HTMLElement = debug.queryAll(By.css('ul > li'))[i].nativeElement;
      expect(componentElement.textContent).toContain(component.jugadorDetail.competenciasParticipadas[i].nombre);
    }
  });

  it('should have a tag with component.jugadorDetail.logrosObtenidos[i].descripcion', () => {
    for (let i = 0; i < component.jugadorDetail.logrosObtenidos.length; i++) {
      const componentElement: HTMLElement = debug.queryAll(By.css('ul > li'))[i].nativeElement;
      expect(componentElement.textContent).toContain(component.jugadorDetail.logrosObtenidos[i].descripcion);
    }
  });
  */
});
