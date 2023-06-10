/* archivo src/app/app.module.ts */
import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { TorneoModule } from './torneo/torneo.module';
import { EquipoModule } from './equipo/equipo.module';
import { CompetenciaModule } from './competencia/competencia.module';
import { ResultadoFinalModule } from './resultadoFinal/resultadoFinal.module';
import { HttpClientModule } from '@angular/common/http';
import { EquipoRoutingModule } from './equipo/equipo-routing/equipo-routing.module';
import { TorneoRoutingModule } from './torneo/torneo-routing/torneo-routing.module';
import { CompetenciaRoutingModule } from './competencia/competencia-routing/competencia-routing.module';
import { NgxPaginationModule } from 'ngx-pagination';
import { JugadorModule } from './jugador/jugador.module';
import { CommonModule } from '@angular/common';
import { JugadorRoutingModule } from './jugador/jugador-routing/jugador-routing.module';
import { InicioRoutingModule } from './inicio/inicio-routing/inicio-routing.module';


@NgModule({
  declarations: [
    AppComponent
   ],
  imports: [
    HttpClientModule,
    BrowserModule,
    AppRoutingModule,
    TorneoModule,
    EquipoModule,
    ResultadoFinalModule,
    CompetenciaModule,
    JugadorModule,
    CommonModule,
    JugadorRoutingModule,
    TorneoRoutingModule,
    EquipoRoutingModule,
    InicioRoutingModule,
    CompetenciaRoutingModule,
    NgxPaginationModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
