import { Jugador } from './jugador';
import { Torneo } from '../torneo/torneo';
import { Competencia } from '../competencia/competencia';
import { Logro } from '../logro/logro';

export class JugadorDetail extends Jugador {
  torneosParticipados: Array<Torneo>;
  competencia: Competencia;
  competenciasParticipadas: Array<Competencia>;
  logrosObtenidos: Array<Logro>;

  constructor(
    id: number,
    nombre: string,
    nickname: string,
    nacionalidad: string,
    fotografia: string,
    fechaNacimiento: Date,
    torneosParticipados: Array<Torneo> = [],
    competencia: Competencia,
    competenciasParticipadas: Array<Competencia> = [],
    logrosObtenidos: Array<Logro> = []
  ) {
    super(id, nombre, nickname, nacionalidad, fotografia, fechaNacimiento);
    this.torneosParticipados = torneosParticipados;
    this.competencia = competencia;
    this.competenciasParticipadas = competenciasParticipadas;
    this.logrosObtenidos = logrosObtenidos;
  }
}
