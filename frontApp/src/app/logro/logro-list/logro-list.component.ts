import { Component, OnInit } from '@angular/core';
import { LogroService } from '../logro.service';
import { LogroDetail } from '../logro-detail';

@Component({
  selector: 'app-logro-list',
  templateUrl: './logro-list.component.html',
  styleUrls: ['./logro-list.component.css']
})
export class LogroListComponent implements OnInit {

  selectedLogro!: LogroDetail;
  selected: boolean = false;
  logros: Array<LogroDetail> = [];
  constructor(private logroService: LogroService) { }

  getLogros(): void {
    this.logroService.getLogros().subscribe((logros) => {
      this.logros = logros;
    });
  }

  onSelected(logro: LogroDetail): void {
    this.selected = true;
    this.selectedLogro = logro;
  }

  ngOnInit() {
    this.getLogros();
  }

}

