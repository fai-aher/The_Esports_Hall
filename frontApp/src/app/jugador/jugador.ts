export class Jugador {
  id: number;
  nombre: string;
  nickname: string;
  nacionalidad: string;
  fotografia: string;
  fechaNacimiento: Date;

  constructor(
    id: number,
    nombre: string,
    nickname: string,
    nacionalidad: string,
    fotografia: string,
    fechaNacimiento: Date
  )
  {
    this.id = id;
    this.nombre = nombre;
    this.nickname = nickname;
    this.nacionalidad = nacionalidad;
    this.fotografia = fotografia;
    this.fechaNacimiento = fechaNacimiento;
  }
}
