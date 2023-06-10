/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement, DebugNode } from '@angular/core';

import { TorneoDetailComponent } from './torneo-detail.component';
import { faker } from '@faker-js/faker';
import { TorneoDetail } from '../torneo-detail';
import { RouterTestingModule } from '@angular/router/testing';
import { HttpClientModule } from '@angular/common/http';


describe('TorneoDetailComponent', () => {
  let component: TorneoDetailComponent;
  let fixture: ComponentFixture<TorneoDetailComponent>;
  let debug: DebugElement;
  let equiposSize: number;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports:[HttpClientModule, RouterTestingModule],
      declarations: [ TorneoDetailComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TorneoDetailComponent);
    component = fixture.componentInstance;

    component.torneoDetail= new TorneoDetail(
        faker.datatype.number(),
        faker.lorem.sentence(),
        faker.date.past(),
        faker.lorem.sentence(),
        faker.image.imageUrl(),
        faker.lorem.sentence(),
        faker.lorem.sentence(),
        faker.lorem.sentence(),
        [], [],
      );

    fixture.detectChanges();
    debug = fixture.debugElement;
  });
  /*
  it('should create', () => {
    expect(component).toBeTruthy();
  });
  /*
  it('should have a p.h3.p-3 element with torneoDetail.nombreTorneo', () => {
    const element: HTMLElement = debug.query(By.css('p.h3.p-3')).nativeElement;
    expect(element.textContent).toContain(component.torneoDetail.nombreTorneo);
  });
  */
  /*
  it('should have an img element with alt= torneoDetail.nombreTorneo', () => {
    expect(debug.query(By.css('img')).attributes['alt']).toEqual(
      component.torneoDetail.nombreTorneo
    );
  });
  /*
  it('should have an img element with src= torneoDetail.imagenRepresentativa', () => {
    expect(debug.query(By.css('img')).attributes['src']).toEqual(
      component.torneoDetail.imagenRepresentativa
    );
  });
  */
  /*

  it('should have 3(Equipos) <a> elements', () => {
    expect(debug.queryAll(By.css('dd.caption > a')).length == 3).toBeTrue();
  });

  it('should have a routerLink=/equipos/equipo.id for each equipo', () => {
    for (let i = 0; i < equiposSize; i++) {
      expect(debug.queryAll(By.css('a'))[i].attributes['ng-reflect-router-link'])
      .toContain('/equipos/' + component.torneoDetail.equiposParticipantes[i].id);
    }
  });

  it('should have a tag with component.torneoDetail.equipos[i].nombre', () => {
    for (let i = 0; i < equiposSize; i++) {
      const componentElement: HTMLElement = debug.queryAll(By.css('dd.caption > a'))[i].nativeElement;
      expect(componentElement.textContent).toContain(component.torneoDetail.equiposParticipantes[i].nombre);
    }
  });
  */
  /*
  it('should have one dd tag for component.torneoDetail.fechaFinalizacion', () => {
    const allDt : DebugElement[]= debug.queryAll(By.css('dt'));
    let nodo = allDt.find((value) => {
      return value.nativeElement.textContent == 'Fecha finalizacion';
    });
    expect(nodo?.nativeElement.nextSibling.textContent).toContain(component.torneoDetail.fechaFinalizacion);
  });
  */
  /*
  it('should have one dd tag for component.torneoDetail.paisRealizacion', () => {
    const allDt : DebugElement[]= debug.queryAll(By.css('dt'));
    let nodo = allDt.find((value) => {
      return value.nativeElement.textContent == 'Pais realizacion';
    });
    expect(nodo?.nativeElement.nextSibling.textContent).toContain(component.torneoDetail.paisRealizacion);
  });

  it('should have one dd tag for component.torneoDetail.organizador', () => {
    const allDt : DebugElement[]= debug.queryAll(By.css('dt'));
    let nodo = allDt.find((value) => {
      return value.nativeElement.textContent == 'Organizador';
    });
    expect(nodo?.nativeElement.nextSibling.textContent).toContain(component.torneoDetail.organizador);
  });
  */
  /*
  it('should have one dd tag for component.torneoDetail.enlacePaginaWeb', () => {
    const allDt : DebugElement[]= debug.queryAll(By.css('dt'));
    let nodo = allDt.find((value) => {
      return value.nativeElement.textContent == 'Pagina web';
    });
    expect(nodo?.nativeElement.nextSibling.textContent).toContain(component.torneoDetail.enlacePaginaWeb);
  });
  */
  /*
  it('should have one dd tag for component.torneoDetail.videojuego', () => {
    const allDt : DebugElement[]= debug.queryAll(By.css('dt'));
    let nodo = allDt.find((value) => {
      return value.nativeElement.textContent == 'Videojuego';
    });
    expect(nodo?.nativeElement.nextSibling.textContent).toContain(component.torneoDetail.videojuego);
  });
  */
});
