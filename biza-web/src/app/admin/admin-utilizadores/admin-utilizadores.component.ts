import { Component, OnInit } from '@angular/core';
import { finalize } from 'rxjs/operators';
import { AdminService, CreateUserRequest, Role, UserResponse, PageResponse } from '../../services/admin.service';

@Component({
  selector: 'app-admin-utilizadores',
  templateUrl: './admin-utilizadores.component.html',
  styleUrls: ['./admin-utilizadores.component.scss']
})
export class AdminUtilizadoresComponent implements OnInit {

  users: UserResponse[] = [];

  form: CreateUserRequest = {
    name: '',
    username: '',
    password: '',
    role: 'OFICIAL_CREDITO'
  };

  roles: Role[] = ['OFICIAL_CREDITO', 'GESTOR_CREDITO', 'ADMIN'];

  loading = false;
  errorMessage = '';
  okMessage = '';

  page = 0;
  size = 10;

  constructor(private admin: AdminService) {}

  ngOnInit(): void {
    this.listar();
  }

  listar(): void {
    this.loading = true;
    this.errorMessage = '';
    this.okMessage = '';

    this.admin.listarUtilizadores(this.page, this.size, 'id,asc')
      .pipe(finalize(() => (this.loading = false)))
      .subscribe({
        next: (res: PageResponse<UserResponse>) => this.users = res.content || [],
        error: (err) => {
          console.error(err);
          this.errorMessage = err?.error?.detail || 'Erro ao listar utilizadores.';
        }
      });
  }

  criar(): void {
    this.okMessage = '';
    this.errorMessage = '';

    if (!this.form.name.trim()) { this.errorMessage = 'Nome é obrigatório.'; return; }
    if (!this.form.username.trim()) { this.errorMessage = 'Username é obrigatório.'; return; }
    if (!this.form.password || this.form.password.length < 6) { this.errorMessage = 'Password mínimo 6 caracteres.'; return; }

    this.loading = true;

    this.admin.criarUtilizador({
      ...this.form,
      name: this.form.name.trim(),
      username: this.form.username.trim(),
    })
      .pipe(finalize(() => (this.loading = false)))
      .subscribe({
        next: () => {
          this.okMessage = 'Utilizador criado com sucesso.';
          this.form = { name: '', username: '', password: '', role: 'OFICIAL_CREDITO' };
          this.listar();
        },
        error: (err) => {
          console.error(err);
          this.errorMessage = err?.error?.message || err?.error?.detail || 'Erro ao criar utilizador.';
        }
      });
  }

  toggleActivo(u: UserResponse): void {
    this.okMessage = '';
    this.errorMessage = '';
    this.loading = true;

    const req$ = u.activo ? this.admin.inativarUtilizador(u.id) : this.admin.ativarUtilizador(u.id);

    req$
      .pipe(finalize(() => (this.loading = false)))
      .subscribe({
        next: () => {
          this.okMessage = u.activo ? 'Utilizador inativado.' : 'Utilizador ativado.';
          this.listar();
        },
        error: (err) => {
          console.error(err);
          this.errorMessage = err?.error?.detail || 'Erro ao alterar estado.';
        }
      });
  }
}
