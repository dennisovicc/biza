import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface PageResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}

export type Role = 'OFICIAL_CREDITO' | 'GESTOR_CREDITO' | 'ADMIN';

export interface UserResponse {
  id: number;
  username: string;
  name: string;
  role: Role;
  activo: boolean;
}

export interface CreateUserRequest {
  name: string;
  username: string;
  password: string;
  role: Role;
}

@Injectable({ providedIn: 'root' })
export class AdminService {
  private baseUrl = 'http://localhost:8080/api/admin';

  constructor(private http: HttpClient) {}

  listarUtilizadores(page = 0, size = 10, sort = 'id,asc'): Observable<PageResponse<UserResponse>> {
    const params = new HttpParams()
      .set('page', page)
      .set('size', size)
      .set('sort', sort);
    return this.http.get<PageResponse<UserResponse>>(`${this.baseUrl}/utilizadores`, { params });
  }

  criarUtilizador(req: CreateUserRequest): Observable<UserResponse> {
    return this.http.post<UserResponse>(`${this.baseUrl}/utilizadores`, req);
  }

  inativarUtilizador(id: number): Observable<any> {
    return this.http.patch(`${this.baseUrl}/utilizadores/${id}/inativar`, {});
  }

  ativarUtilizador(id: number): Observable<any> {
    return this.http.patch(`${this.baseUrl}/utilizadores/${id}/ativar`, {});
  }
}
