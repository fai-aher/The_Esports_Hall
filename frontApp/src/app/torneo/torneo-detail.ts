import { Torneo } from './torneo';
import { Equipo } from '../equipo/equipo';
import { Competencia } from '../competencia/competencia';

export class TorneoDetail extends Torneo {
  equiposParticipantes: Array<Equipo> = [];
  competencias: Array<Competencia> = [];

  constructor(
    id: number,
    nombreTorneo: string,
    fechaFinalizacion: any,
    paisRealizacion: string,
    imagenRepresentativa: string,
    enlacePaginaWeb: string,
    organizador: string,
    videojuego: string,
    equiposParticipantes: Array<Equipo> = [],
    competencias: Array<Competencia> = []
  ) {
    super(id, nombreTorneo, fechaFinalizacion, paisRealizacion, imagenRepresentativa, enlacePaginaWeb, organizador, videojuego);
    this.equiposParticipantes = equiposParticipantes;
    this.competencias = competencias;
  }
}
