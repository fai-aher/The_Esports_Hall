import { Component, OnInit } from '@angular/core';
import { JugadorService } from '../jugador.service';
import { JugadorDetail } from '../jugador-detail';

@Component({
  selector: 'app-jugador-list',
  templateUrl: './jugador-list.component.html',
  styleUrls: ['./jugador-list.component.css']
})
export class JugadorListComponent implements OnInit {

  selectedJugador!: JugadorDetail;
  selected: boolean = false;
  jugadores: Array<JugadorDetail> = [];
  constructor(private jugadorService: JugadorService) { }

  getJugadores(): void {
    this.jugadorService.getJugadores().subscribe((jugadores) => {
      this.jugadores = jugadores;
    });
  }

  onSelected(jugador: JugadorDetail): void {
    this.selected = true;
    this.selectedJugador = jugador;
  }

  ngOnInit() {
    this.getJugadores();
  }

}
