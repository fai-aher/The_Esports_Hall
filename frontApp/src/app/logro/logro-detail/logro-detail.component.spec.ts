/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

import { LogroDetailComponent } from './logro-detail.component';
import { HttpClientModule } from '@angular/common/http';
import { RouterTestingModule } from '@angular/router/testing';
import { LogroService } from '../logro.service';
import { Jugador } from 'src/app/jugador/jugador';
import { faker } from '@faker-js/faker';
import { LogroDetail } from '../logro-detail';

describe('LogroDetailComponent', () => {
  let component: LogroDetailComponent;
  let fixture: ComponentFixture<LogroDetailComponent>;
  let debug: DebugElement;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientModule, RouterTestingModule],
      declarations: [ LogroDetailComponent ],
      providers: [ LogroService ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LogroDetailComponent);
    component = fixture.componentInstance;

    let jugador = new Jugador(
      faker.datatype.number(),
      faker.lorem.sentence(),
      faker.lorem.sentence(),
      faker.lorem.sentence(),
      faker.image.imageUrl(),
      faker.date.past()
    );

    component.logroDetail = new LogroDetail(
      faker.datatype.number(),
      faker.lorem.sentence(),
      jugador
    );

    fixture.detectChanges();
    debug = fixture.debugElement;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
  /*
  it('should have an img element with alt= logroDetail.descripcion', () => {
    expect(debug.query(By.css('img')).attributes['alt']).toEqual(
      component.logroDetail.descripcion
   );
  });

  it('should have a tag with component.logroDetail.jugador.nombre', () => {
    const componentElement: HTMLElement = debug.query(By.css('p.h3.p-2.author-name')).nativeElement;
    expect(componentElement.textContent).toContain(component.logroDetail.jugador.nombre);
  });
  */
});
