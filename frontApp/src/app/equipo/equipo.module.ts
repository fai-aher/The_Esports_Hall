/* Archivo src/app/equipo/equipo.module.ts*/

import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { EquipoListComponent } from './equipo-list/equipo-list.component';
import { EquipoDetailComponent } from './equipo-detail/equipo-detail.component';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    CommonModule,
    RouterModule
  ],
  exports: [
    EquipoListComponent
  ],
  declarations: [
    EquipoListComponent, EquipoDetailComponent
  ]
})
export class EquipoModule { }

