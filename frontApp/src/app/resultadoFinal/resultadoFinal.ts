/* archivo src/app/resultadoFinal/resultadoFinal.ts */

import { Equipo } from "../equipo/equipo";
import { Competencia } from "../competencia/competencia";

/* This is the TS class for the Resultado Final Resource.
   it has the following attibutes:
  - id
  - nombre
  - paisProcedencia
  - banderaPais
  - logo
*/


export class ResultadoFinal {
  id: number;
  posicionFinal: number;
  parteDeEmpate: boolean;
  equipoInvolucrado: Equipo;
  competenciaRelacionada: Competencia;

  constructor(
    id: number,
    posicionFinal: number,
    parteDeEmpate: boolean,
    equipoInvolucrado: Equipo,
    competenciaRelacionada: Competencia
  ) {
    this.id = id;
    this.posicionFinal = posicionFinal;
    this.parteDeEmpate = parteDeEmpate;
    this.equipoInvolucrado = equipoInvolucrado;
    this.competenciaRelacionada = competenciaRelacionada;
  }
}
