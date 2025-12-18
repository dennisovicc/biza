import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export type Role = 'OFICIAL_CREDITO' | 'GESTOR_CREDITO' | 'ADMIN';

export interface LoginRequest {
  username: string;
  password: string;
}

export interface AuthUser {
  id: number;
  username: string;
  name: string;
  role: Role;
}

export interface LoginResponse {
  token: string;
  user: AuthUser;
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  private baseUrl = 'http://localhost:8080/api/auth';

  constructor(private http: HttpClient) {}

  login(req: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.baseUrl}/login`, req);
  }

  setSession(res: LoginResponse): void {
    localStorage.setItem('token', res.token);
    localStorage.setItem('user', JSON.stringify(res.user));
  }

  logout(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
  }

  isLoggedIn(): boolean {
    return !!localStorage.getItem('token');
  }

  getUser(): AuthUser | null {
    const raw = localStorage.getItem('user');
    return raw ? JSON.parse(raw) : null;
  }

  hasAnyRole(roles: string[]): boolean {
    const u = this.getUser();
    return !!u && roles.includes(u.role);
  }

  getDisplayName(): string {
    const u = this.getUser();
    return u?.name || u?.username || '';
  }
    getToken(): string | null {
    return localStorage.getItem('token');
  }

  getUsername(): string {
    const u = this.getUser();
    return u?.name || u?.username || '';
  }

  getRole(): string {
    const u = this.getUser();
    return u?.role || '';
  }

}
