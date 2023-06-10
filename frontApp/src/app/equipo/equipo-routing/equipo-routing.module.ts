import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { EquipoListComponent } from '../equipo-list/equipo-list.component';
import { EquipoDetailComponent } from '../equipo-detail/equipo-detail.component';

const routes: Routes = [{
  path: 'equipos',
  children: [
    {
      path: 'list',
      component: EquipoListComponent
    },
    {
      path: ':id',
      component: EquipoDetailComponent
    },
  ]
 }];


@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class EquipoRoutingModule { }
