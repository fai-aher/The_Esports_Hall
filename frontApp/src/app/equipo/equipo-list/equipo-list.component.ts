import { Component, OnInit, EventEmitter, Output } from '@angular/core';
import { EquipoService } from '../equipo.service';
import { EquipoDetail } from '../equipo-detail';
@Component({
  selector: 'app-equipo-list',
  templateUrl: './equipo-list.component.html',
  styleUrls: ['./equipo-list.component.css']
})
export class EquipoListComponent implements OnInit {

  //Attributes to show the Detail of a Equipo
  selectedEquipo!: EquipoDetail;
  selected: boolean = false;
  equipos: Array<EquipoDetail> = [];

  //Attributes for additional functionalities

  public searchQuery: string = '';
  @Output() filteredEquipos: EventEmitter<string> = new EventEmitter<string>();



  // All the rest of the Component logic
  constructor(private equipoService: EquipoService) { }

  getEquipos(): void {
    this.equipoService.getEquipos().subscribe((equipos) => {
      this.equipos = equipos;
    });
  }

  //On selected for showing the details when image clicked.
  onSelected(equipo: EquipoDetail): void {
    this.selected = true;
    this.selectedEquipo = equipo;
  }

  // Methods for additional functionalities

  ngOnInit() {
    this.getEquipos();
  }

}
