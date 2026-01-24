import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Pregunta } from '../models/pregunta';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class TrivialService {
  //private apiUrl = 'http://localhost:8080/api/trivial';
  private apiUrl = `${environment.apiUrl}/api/trivial`;
  

  constructor(private http: HttpClient) {}

getPreguntas(cantidad: number = 5, categoria?: string): Observable<Pregunta[]> {
  let url = `${this.apiUrl}/preguntas?cantidad=${cantidad}`;
  if (categoria) {
    url += `&categoria=${encodeURIComponent(categoria)}`;
  }
  return this.http.get<Pregunta[]>(url);
}


  getCategorias(): Observable<string[]> {
    return this.http.get<string[]>(`${this.apiUrl}/categorias`);
  }
}
