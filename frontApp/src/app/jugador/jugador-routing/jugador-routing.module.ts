import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { JugadorListComponent } from '../jugador-list/jugador-list.component';
import { JugadorDetailComponent } from '../jugador-detail/jugador-detail.component';

const routes: Routes = [{
  path: 'jugadores',
  children: [
    {
      path: 'list',
      component: JugadorListComponent
    },
    {
      path: ':id',
      component: JugadorDetailComponent
    },
  ]
 }];


@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class JugadorRoutingModule { }
