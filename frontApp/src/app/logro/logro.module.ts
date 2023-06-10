/* Archivo src/app/equipo/equipo.module.ts*/

import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LogroListComponent } from './logro-list/logro-list.component';
import { LogroDetailComponent } from './logro-detail/logro-detail.component';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    CommonModule, RouterModule
  ],
  exports: [LogroListComponent],
  declarations: [LogroListComponent, LogroDetailComponent]
})
export class LogroModule { }
