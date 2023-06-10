import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { ResultadoFinal } from './resultadoFinal';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ResultadoFinalService {

  private apiUrl: string = environment.baseUrl + 'books';

  constructor(private http: HttpClient) { }

  getResultadosFinales(): Observable<ResultadoFinal[]> {
    return this.http.get<ResultadoFinal[]>(this.apiUrl);
  }

}
