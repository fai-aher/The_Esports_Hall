import { Component, OnInit } from '@angular/core';
import { TorneoDetail } from '../torneo-detail';
import { TorneoService } from '../torneo.service';

@Component({
  selector: 'app-torneo-list',
  templateUrl: './torneo-list.component.html',
  styleUrls: ['./torneo-list.component.css']
})
export class TorneoListComponent implements OnInit {

  torneos: Array<TorneoDetail> = [];
  selected: boolean = false;
  selectedTorneo!: TorneoDetail;
  p: number = 1;

  /* Attributes for the Search Filter Functionality */
  searchTerm: string = '';
  selectedFilter: string = 'nombre';

  constructor(private torneoService: TorneoService) { }

  getTorneos(): void {
    this.torneoService.getTorneos().subscribe((torneos) => {
      this.torneos = torneos;
    });
  }

  onSelected(torneo: TorneoDetail): void {
    this.selected = true;
    this.selectedTorneo = torneo;
  }

  ordenarTorneos(): void {
    this.torneos.sort((a, b) => a.nombreTorneo.localeCompare(b.nombreTorneo));
  }

  /* Methods to enable the Search Filter */
  filterTorneos(): void {
    const searchTerm = this.searchTerm.toLowerCase();

    if (searchTerm) {
      this.torneos = this.torneos.filter((torneo) => {
        switch (this.selectedFilter) {
          case 'nombre':
            return torneo.nombreTorneo.toLowerCase().includes(searchTerm);
          case 'organizador':
            return torneo.organizador.toLowerCase().includes(searchTerm);
          case 'videojuego':
            return torneo.videojuego.toLowerCase().includes(searchTerm);
          default:
            return true; // No se seleccionó un filtro válido, muestra todos los torneos
        }
      });
    } else {
      this.resetTorneos(); // Obtener la lista completa de torneos en cualquier caso.
    }
  }

  getConteoTotalTorneos(): number {
    return this.torneos.length;
  }


  getVideojuegoMasFrecuente(): string {
    const videojuegos: string[] = this.torneos.map((torneo) => torneo.videojuego);
    const videojuegoFrecuente = videojuegos.reduce((acc: any, curr: any) => {
      if (typeof acc[curr] === 'undefined') {
        acc[curr] = 1;
      } else {
        acc[curr] += 1;
      }
      return acc;
    }, {});

    const values = Object.values(videojuegoFrecuente);
    const validValues = values.filter((value): value is number => typeof value === 'number');
    const maxCount = Math.max(...validValues);

    const videojuegoMasFrecuente = Object.keys(videojuegoFrecuente).find(
      (key) => videojuegoFrecuente[key] === maxCount
    );

    return videojuegoMasFrecuente || '';
  }

  resetTorneos(): void {
    this.getTorneos();
  }

  /* On Init */

  ngOnInit() {
    this.getTorneos();
    this.filterTorneos();
    }


}
