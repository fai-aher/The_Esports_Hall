import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TorneoListComponent } from './torneo-list/torneo-list.component';
import { TorneoDetailComponent } from './torneo-detail/torneo-detail.component';
import { RouterModule } from '@angular/router';
import {NgxPaginationModule} from 'ngx-pagination';
import { FormsModule } from '@angular/forms';

@NgModule({
  imports: [CommonModule, RouterModule, NgxPaginationModule, FormsModule],
  exports: [TorneoListComponent],
  declarations: [TorneoDetailComponent, TorneoListComponent]
})
export class TorneoModule {}
