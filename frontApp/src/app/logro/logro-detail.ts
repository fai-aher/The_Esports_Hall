import { Logro } from './logro';
import { Jugador } from '../jugador/jugador';

export class LogroDetail extends Logro {
  jugador: Jugador;

  constructor(
    id: number,
    descripcion: string,
    jugador: Jugador
  ) {
    super(id, descripcion);
    this.jugador = jugador;
  }
}

