import { format } from 'date-fns';

export class Torneo {
  id: number;
  nombreTorneo: string;
  fechaFinalizacion: string;
  paisRealizacion: string;
  imagenRepresentativa: string;
  enlacePaginaWeb: string;
  organizador: string;
  videojuego: string;

  constructor(
    id: number,
    nombreTorneo: string,
    fechaFinalizacion: any,
    paisRealizacion: string,
    imagenRepresentativa: string,
    enlacePaginaWeb: string,
    organizador: string,
    videojuego: string
  ) {
    this.id = id;
    this.nombreTorneo = nombreTorneo;
    this.fechaFinalizacion = format(new Date(fechaFinalizacion), 'dd/MM/yyyy');
    this.paisRealizacion = paisRealizacion;
    this.imagenRepresentativa = imagenRepresentativa;
    this.enlacePaginaWeb = enlacePaginaWeb;
    this.organizador = organizador;
    this.videojuego = videojuego;
  }
}
