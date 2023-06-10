import { HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { AppComponent } from './app.component';
import { EquipoListComponent } from './equipo/equipo-list/equipo-list.component';
import { ResultadoFinalListComponent } from './resultadoFinal/resultadoFinal-list/resultadoFinal-list.component';
import { CompetenciaListComponent } from './competencia/competencia-list/competencia-list.component';
import { TorneoListComponent } from './torneo/torneo-list/torneo-list.component';
import { JugadorListComponent } from './jugador/jugador-list/jugador-list.component';

describe('AppComponent', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RouterTestingModule, HttpClientModule],
      declarations: [AppComponent, EquipoListComponent, ResultadoFinalListComponent, CompetenciaListComponent, TorneoListComponent, JugadorListComponent],
    }).compileComponents();
  });

  it('should create the app', () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.componentInstance;
    expect(app).toBeTruthy();
  });

  it(`should have as title 'base-project'`, () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.componentInstance;
    expect(app.title).toEqual('base-project');
  });

});
