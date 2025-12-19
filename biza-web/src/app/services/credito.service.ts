import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Credito } from '../models/credito.model';
import { PageResponse } from '../models/page-response.model';

export interface CriarCreditoRequest {
  clienteId: number;
  tipoCredito: 'RAPIDO' | 'LONGO';
  montante: number;
  prazoMeses: number;
}

@Injectable({ providedIn: 'root' })
export class CreditoService {
  private baseUrl = 'http://localhost:8080/api/v1/creditos';

  constructor(private http: HttpClient) {}

  criar(req: CriarCreditoRequest): Observable<Credito> {
    return this.http.post<Credito>(this.baseUrl, req);
  }

  listar(clienteId?: number, status?: string): Observable<PageResponse<Credito>> {
    let params = new HttpParams();
    if (clienteId != null) params = params.set('clienteId', clienteId);
    if (status != null && status !== '') params = params.set('status', status);
    return this.http.get<PageResponse<Credito>>(this.baseUrl, { params });
  }

  aprovar(id: string) {
    return this.http.patch<Credito>(`${this.baseUrl}/${id}/aprovar`, {});
  }

  rejeitar(id: string) {
    return this.http.patch<Credito>(`${this.baseUrl}/${id}/rejeitar`, {});
  }

  liberar(id: string) {
    return this.http.patch<Credito>(`${this.baseUrl}/${id}/liberar`, {});
  }

  liquidar(id: string) {
    return this.http.patch<Credito>(`${this.baseUrl}/${id}/liquidar`, {});
  }

  atualizarSaldo(id: string) {
    return this.http.patch<Credito>(`${this.baseUrl}/${id}/atualizar-saldo`, {});
  }
}
