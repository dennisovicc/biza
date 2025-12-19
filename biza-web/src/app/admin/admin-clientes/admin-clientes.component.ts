import { Component, OnInit } from '@angular/core';
import { finalize } from 'rxjs/operators';
import { ClienteService } from '../../services/cliente.service';

interface PageResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}

type StatusRegistro = 'ACTIVO' | 'INACTIVO';

interface ClienteRow {
  id: number;
  nome: string;
  nuit?: string;
  telefone?: string;
  email?: string;
  status: StatusRegistro;
}

@Component({
  selector: 'app-admin-clientes',
  templateUrl: './admin-clientes.component.html',
  styleUrls: ['./admin-clientes.component.scss']
})
export class AdminClientesComponent implements OnInit {

  clientes: ClienteRow[] = [];
  loading = false;
  errorMessage = '';
  okMessage = '';

  constructor(private clienteService: ClienteService) {}

  ngOnInit(): void {
    this.listar();
  }

  listar(): void {
    this.loading = true;
    this.errorMessage = '';
    this.okMessage = '';

    this.clienteService.listarClientes()
      .pipe(finalize(() => (this.loading = false)))
      .subscribe({
        next: (res: PageResponse<any>) => this.clientes = (res.content || []) as ClienteRow[],
        error: (err) => {
          console.error(err);
          this.errorMessage = err?.error?.detail || 'Erro ao listar clientes.';
        }
      });
  }

  inativar(c: ClienteRow): void {
    this.loading = true;
    this.errorMessage = '';
    this.okMessage = '';

    this.clienteService.inativarCliente(c.id)
      .pipe(finalize(() => (this.loading = false)))
      .subscribe({
        next: () => { this.okMessage = 'Cliente inativado.'; this.listar(); },
        error: (err) => {
          console.error(err);
          this.errorMessage = err?.error?.detail || 'Erro ao inativar cliente.';
        }
      });
  }
}
