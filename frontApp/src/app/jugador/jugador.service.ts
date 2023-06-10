import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { Observable, catchError, throwError } from 'rxjs';
import { JugadorDetail } from './jugador-detail';

@Injectable({
  providedIn: 'root'
})
export class JugadorService {

  private apiUrl: string = environment.baseUrl + 'jugadores';

  constructor(private http: HttpClient) { }

  getJugadores(): Observable<JugadorDetail[]> {
    return this.http
      .get<JugadorDetail[]>(this.apiUrl)
      .pipe(
        catchError((err) => throwError(() => new Error('error en el servicio')))
      );
  }

  getJugador(id: string): Observable<JugadorDetail> {
    return this.http.get<JugadorDetail>(this.apiUrl + '/' + id);
  }
}
