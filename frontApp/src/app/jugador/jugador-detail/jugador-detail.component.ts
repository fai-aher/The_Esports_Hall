import { Component, Input, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JugadorDetail } from '../jugador-detail';
import { JugadorService } from '../jugador.service';

@Component({
  selector: 'app-jugador-detail',
  templateUrl: './jugador-detail.component.html',
  styleUrls: ['./jugador-detail.component.css']
})
export class JugadorDetailComponent implements OnInit {

  jugadorId!: string;
  @Input() jugadorDetail!: JugadorDetail;

  constructor(
    private route: ActivatedRoute,
    private jugadorService: JugadorService) { }

  getJugador() {
    this.jugadorService.getJugador(this.jugadorId).subscribe(
      (jugadorDetail: JugadorDetail) => {
        this.jugadorDetail = jugadorDetail;
      },
      (error) => {
        console.error(error);
      }
    );
  }

  ngOnInit() {
    if (!this.jugadorDetail) {
      this.jugadorId = this.route.snapshot.paramMap.get('id')!;
      if (this.jugadorId) {
        this.getJugador();
      }
    }
  }
}
