import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { TorneoListComponent } from '../torneo-list/torneo-list.component';
import { TorneoDetailComponent } from '../torneo-detail/torneo-detail.component';

const routes: Routes = [{
  path: 'torneos',
  children: [
    {
      path: 'list',
      component: TorneoListComponent
    },
    {
      path: ':id',
      component: TorneoDetailComponent
    },
  ]
 }];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class TorneoRoutingModule { }
