import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Pagamento } from '../models/pagamento.model';

interface PageResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}

export interface RegistrarPagamentoRequest {
  creditoId: string;
  valor: number;
  dataPagamento?: string | null;
}

@Injectable({
  providedIn: 'root'
})
export class PagamentoService {

  private apiUrl = 'http://localhost:8080/api/v1/pagamentos';

  constructor(private http: HttpClient) {}

  listar(creditoId?: string | null): Observable<PageResponse<Pagamento>> {
    let params = new HttpParams();
    if (creditoId) {
      params = params.set('creditoId', creditoId);
    }
    return this.http.get<PageResponse<Pagamento>>(this.apiUrl, { params });
  }

  registrar(request: RegistrarPagamentoRequest): Observable<Pagamento> {
    return this.http.post<Pagamento>(this.apiUrl, request);
  }
}
