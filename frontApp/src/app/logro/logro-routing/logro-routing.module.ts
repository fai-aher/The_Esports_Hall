import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LogroRoutingComponent } from './logro-routing.component';
import { RouterModule, Routes } from '@angular/router';
import { LogroListComponent } from '../logro-list/logro-list.component';

const routes: Routes = [{
  path: 'logros',
  children: [
    {
      path: 'list',
      component: LogroListComponent
    },
    {
      path: ':id',
      component: LogroListComponent
    },
  ]
 }];


@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class LogroRoutingModule { }
