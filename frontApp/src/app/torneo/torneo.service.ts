import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { catchError, Observable, throwError } from 'rxjs';
import { TorneoDetail } from './torneo-detail';

@Injectable({
  providedIn: 'root'
})
export class TorneoService {

  private apiUrl: string = environment.baseUrl + 'torneos';

  constructor(private http: HttpClient) {}

  getTorneos(): Observable<TorneoDetail[]> {
    return this.http
      .get<TorneoDetail[]>(this.apiUrl)
      .pipe(
        catchError((err) => throwError(() => new Error('error en el servicio')))
      );
  }

  getTorneo(id: string): Observable<TorneoDetail> {
    return this.http.get<TorneoDetail>(this.apiUrl + '/' + id);
  }

  }
