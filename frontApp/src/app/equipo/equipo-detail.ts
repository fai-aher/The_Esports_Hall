/* archivo src/app/equipo/equipo-detail.ts */

/* This is the TS class for the Equipo Detail Resource.

  EquipoDetail has the following attributes:
  - torneosParticipados
  - competenciasParticipadas
  - competenciasGanadas
  - resultadosEnCompetencias
  - integrantes
*/
import { Equipo} from "../equipo/equipo";
import { Torneo } from "../torneo/torneo";
import { Competencia } from "../competencia/competencia";
import { ResultadoFinal } from "../resultadoFinal/resultadoFinal";
import { Jugador } from "../jugador/jugador";


export class EquipoDetail extends Equipo {

  torneosParticipados: Array<Torneo> = [];
  competenciasParticipadas: Array<Competencia> = [];
  competenciasGanadas: Array<Competencia> = [];
  resultadosEnCompetencias: Array<ResultadoFinal> = [];
  integrantes: Array<Jugador> = [];


  constructor(
    id: number,
    nombre: string,
    paisProcedencia: string,
    banderaPais: string,
    logo: string,
    torneosParticipados: Array<Torneo> = [],
    competenciasParticipadas: Array<Competencia> = [],
    competenciasGanadas: Array<Competencia> = [],
    resultadosEnCompetencias: Array<ResultadoFinal> = [],
    integrantes: Array<Jugador> = []
  ) {
    super(id, nombre, paisProcedencia, banderaPais, logo);
    this.torneosParticipados = torneosParticipados;
    this.competenciasParticipadas = competenciasParticipadas;
    this.competenciasGanadas = competenciasGanadas;
    this.resultadosEnCompetencias = resultadosEnCompetencias;
    this.integrantes = integrantes;
  }
}


