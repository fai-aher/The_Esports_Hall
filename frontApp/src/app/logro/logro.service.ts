import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { Observable } from 'rxjs';
import { LogroDetail } from './logro-detail';
import { Logro } from './logro';

@Injectable({
  providedIn: 'root'
})
export class LogroService {

  private apiUrl: string = environment.baseUrl + 'logros';

  constructor(private http: HttpClient) { }

  getLogros(): Observable<LogroDetail[]> {
    return this.http.get<LogroDetail[]>(this.apiUrl);
  }

  getLogro(id: string): Observable<LogroDetail> {
    return this.http.get<LogroDetail>(this.apiUrl + '/' + id);
  }

  createJugador(logro: Logro): Observable<Logro>{
    return this.http.post<Logro>(this.apiUrl, logro);
  }



}
