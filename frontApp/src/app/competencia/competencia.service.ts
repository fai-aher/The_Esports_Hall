import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { catchError, Observable, throwError } from 'rxjs';
import { CompetenciaDetail } from './competencia-detail';

@Injectable({
  providedIn: 'root'
})
export class CompetenciaService {

  private apiUrl: string = environment.baseUrl + 'competencias';

  constructor(private http: HttpClient) { }

  getCompetencias(): Observable<CompetenciaDetail[]> {
    return this.http
      .get<CompetenciaDetail[]>(this.apiUrl)
      .pipe(
        catchError((err) => throwError(() => new Error('error en el servicio')))
      );
  }

  //In order to implement the router /competencias/:id
  getCompetencia(id: string): Observable<CompetenciaDetail> {
    return this.http.get<CompetenciaDetail>(this.apiUrl + "/" + id);
  }

}
