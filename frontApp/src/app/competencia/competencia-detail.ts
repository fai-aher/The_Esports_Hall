/* archivo src/app/competencia/competencia-detail.ts */

import { Competencia} from "../competencia/competencia";
import { ResultadoFinal } from "../resultadoFinal/resultadoFinal";
import { Equipo } from "../equipo/equipo";
import { Torneo } from "../torneo/torneo";
import { Jugador } from "../jugador/jugador";

export class CompetenciaDetail extends Competencia {

  resultadosFinales: Array<ResultadoFinal> = [];
  equiposParticipantes: Array<Equipo> = [];

  constructor(

    id: number,
    nombre: string,
    duracion: string,
    equipoGanador: Equipo,
    jugador: Jugador,
    mvp: Jugador,
    torneo: Torneo,
    resultadosFinales: Array<ResultadoFinal> = [],
    equiposParticipantes: Array<Equipo> = [],


  ) {
    super(id, nombre, duracion, equipoGanador,jugador, mvp, torneo);
    this.resultadosFinales = resultadosFinales;
    this.equiposParticipantes = equiposParticipantes;

  }
}
