import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Credito } from '../models/credito.model';

interface PageResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}

export interface CriarCreditoRequest {
  clienteId: string;
  montante: number;
  taxaJurosAnual: number;
  prazoMeses: number;
}

@Injectable({
  providedIn: 'root'
})
export class CreditoService {

  private apiUrl = 'http://localhost:8080/api/v1/creditos';

  constructor(private http: HttpClient) {}

  listar(clienteId?: string | null): Observable<PageResponse<Credito>> {
    let params = new HttpParams();
    if (clienteId) {
      params = params.set('clienteId', clienteId);
    }
    return this.http.get<PageResponse<Credito>>(this.apiUrl, { params });
  }

  criar(request: CriarCreditoRequest): Observable<Credito> {
    return this.http.post<Credito>(this.apiUrl, request);
  }
}
