/* Archivo src/app/equipo/equipo.module.ts*/

import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { JugadorListComponent } from './jugador-list/jugador-list.component';
import { JugadorDetailComponent } from './jugador-detail/jugador-detail.component';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    CommonModule, RouterModule
  ],
  exports: [JugadorListComponent],
  declarations: [JugadorListComponent, JugadorDetailComponent]
})
export class JugadorModule { }
