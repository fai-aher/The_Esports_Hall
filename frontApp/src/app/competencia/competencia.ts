import { Equipo } from "../equipo/equipo";
import { Jugador } from "../jugador/jugador";
import { Torneo } from "../torneo/torneo";

export class Competencia {

  id: number;
  nombre: string;
  duracion: string;
  equipoGanador: Equipo;
  jugador: Jugador;
  mvp: Jugador;
  torneo: Torneo;

  constructor(
    id: number,
    nombre: string,
    duracion: string,
    equipoGanador: Equipo,
    jugador: Jugador,
    mvp: Jugador,
    torneo: Torneo,

  ) {
    this.id = id;
    this.nombre = nombre;
    this.duracion = duracion;
    this.equipoGanador = equipoGanador;
    this.jugador = jugador;
    this.mvp = mvp;
    this.torneo = torneo;
  }
}
