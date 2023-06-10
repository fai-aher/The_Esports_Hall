import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { Observable } from 'rxjs';
import { EquipoDetail } from './equipo-detail';
@Injectable({
  providedIn: 'root'
})
export class EquipoService {

  private apiUrl: string = environment.baseUrl + 'equipos';

  constructor(private http: HttpClient) { }

  getEquipos(): Observable<EquipoDetail[]> {
    return this.http.get<EquipoDetail[]>(this.apiUrl);
  }

  //In order to implement the router /equipos/:id
  getEquipo(id: string): Observable<EquipoDetail> {
    return this.http.get<EquipoDetail>(this.apiUrl + "/" + id);
  }

}
