import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { CompetenciaListComponent } from '../competencia-list/competencia-list.component';
import { CompetenciaDetailComponent } from '../competencia-detail/competencia-detail.component';

const routes: Routes = [{
  path: 'competencias',
  children: [
    {
      path: 'list',
      component: CompetenciaListComponent
    },
    {
      path: ':id',
      component: CompetenciaDetailComponent
    },
  ]
 }];


@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class CompetenciaRoutingModule { }
