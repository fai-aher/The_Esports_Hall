import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CompetenciaListComponent } from './competencia-list/competencia-list.component';
import { CompetenciaDetailComponent } from './competencia-detail/competencia-detail.component';
import { RouterModule } from '@angular/router';
import {NgxPaginationModule} from 'ngx-pagination';
import { FormsModule } from '@angular/forms';

@NgModule({
  imports: [CommonModule, RouterModule, NgxPaginationModule, FormsModule],
  exports: [
    CompetenciaListComponent
  ],
  declarations: [
    CompetenciaListComponent, CompetenciaDetailComponent
  ]
})
export class CompetenciaModule { }
