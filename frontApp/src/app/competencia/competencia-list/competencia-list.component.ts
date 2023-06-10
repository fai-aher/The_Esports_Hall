import { Component, OnInit } from '@angular/core';

import { CompetenciaService } from '../competencia.service';
import { CompetenciaDetail } from '../competencia-detail';

@Component({
  selector: 'app-competencia-list',
  templateUrl: './competencia-list.component.html',
  styleUrls: ['./competencia-list.component.css']
})
export class CompetenciaListComponent implements OnInit {

  competencias: Array<CompetenciaDetail> = [];
  selected: boolean = false;
  selectedCompetencia!: CompetenciaDetail;
  p: number = 1;
  searchedCompetencia: any;

  constructor(private competenciaService: CompetenciaService) { }

  getCompetencias(): void {
    this.competenciaService.getCompetencias().subscribe((competencias) => {
      this.competencias = competencias;
    });
  }

  onSelected(competencia: CompetenciaDetail): void {
    this.selected = true;
    this.selectedCompetencia = competencia;
  }

  ngOnInit() {
    this.getCompetencias();
  }

}
