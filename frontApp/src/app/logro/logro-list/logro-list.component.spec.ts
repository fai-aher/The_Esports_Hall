/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { asNativeElements, DebugElement } from '@angular/core';
import { faker } from '@faker-js/faker';

import { LogroListComponent } from './logro-list.component';
import { HttpClientModule } from '@angular/common/http';
import { LogroService } from '../logro.service';
import { LogroDetail } from '../logro-detail';
import { RouterTestingModule } from '@angular/router/testing';
import { Jugador } from 'src/app/jugador/jugador';

describe('LogroListComponent', () => {
  let component: LogroListComponent;
  let fixture: ComponentFixture<LogroListComponent>;
  let debug: DebugElement;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports:[HttpClientModule, RouterTestingModule],
      declarations: [ LogroListComponent ],
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LogroListComponent);
    component = fixture.componentInstance;

    let testLogros: Array<LogroDetail> = [];
    let jugador = new Jugador(
      faker.datatype.number(),
      faker.lorem.sentence(),
      faker.lorem.sentence(),
      faker.lorem.sentence(),
      faker.image.imageUrl(),
      faker.date.past()
    );

    for(let i = 0; i<10; i++) {
      testLogros[i] = new LogroDetail(
        faker.datatype.number(),
        faker.lorem.sentence(),
        jugador
    );
    }

    component.logros = testLogros;
    fixture.detectChanges();
    debug = fixture.debugElement;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should have 10 <div.col.mb-2> elements', () => {
    expect(debug.queryAll(By.css('div.col.mb-2')).length == 10).toBeTrue();
  });

  it('should have 10 <card.p-2> elements', () => {
    expect(debug.queryAll(By.css('div.card.p-2')).length == 10).toBeTrue();
  });

  it('should have 10 <div.card-body> elements', () => {
    expect(debug.queryAll(By.css('div.card-body')).length == 10).toBeTrue();
  });

  it('should have p tag with the logro.descripcion', () => {
    debug.queryAll(By.css('p')).forEach((p, i)=>{
      expect(p.nativeElement.textContent).toContain(component.logros[i].descripcion)
    });
  });

  it('should have 9 <div.col.mb-2> elements and the deleted jugador should not exist', () => {
  const logro = component.logros.pop()!;
  fixture.detectChanges();
  expect(debug.queryAll(By.css('div.col.mb-2'))).toHaveSize(9)

  debug.queryAll(By.css('div.col.mb-2')).forEach((selector, i)=>{
    expect(selector.nativeElement.textContent).not.toContain(logro.descripcion);
  });
});
});

