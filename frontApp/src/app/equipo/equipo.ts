/* archivo src/app/equipo/equipo.ts */

/* This is the TS class for the Equipo Resource.
  Equipo has the following attibutes:
  - id
  - nombre
  - paisProcedencia
  - banderaPais
  - logo

  EquipoDetail has the following attributes:
  - torneosParticipados
  - competenciasParticipadas
  - competenciasGanadas
  - resultadosEnCompetencias
  - integrantes
*/


export class Equipo {
  id: number;
  nombre: string;
  paisProcedencia: string;
  banderaPais: string;
  logo: string;

  constructor(
    id: number,
    nombre: string,
    paisProcedencia: string,
    banderaPais: string,
    logo: string,
  ) {
    this.id = id;
    this.nombre = nombre;
    this.paisProcedencia = paisProcedencia;
    this.banderaPais = banderaPais;
    this.logo = logo;
  }
}
