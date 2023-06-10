import { Component, OnInit } from '@angular/core';
import { ResultadoFinal } from '../resultadoFinal';
import { ResultadoFinalService } from '../resultadoFinal.service';

@Component({
  selector: 'app-resultadoFinal-list',
  templateUrl: './resultadoFinal-list.component.html',
  styleUrls: ['./resultadoFinal-list.component.css']
})
export class ResultadoFinalListComponent implements OnInit {

  resultadosFinales: Array<ResultadoFinal> = [];

  constructor(private resultadoFinalService: ResultadoFinalService) { }

  getResultadosFinales(): void {
    this.resultadoFinalService.getResultadosFinales().subscribe((resultadosFinales) => {
      this.resultadosFinales = resultadosFinales;
    });
  }

  ngOnInit() {
    this.getResultadosFinales();
  }

}
