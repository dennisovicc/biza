import { Component } from '@angular/core';
import { ClienteService } from '../services/cliente.service';
import { Cliente } from '../models/cliente.model';

interface PageResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}

@Component({
  selector: 'app-clientes',
  templateUrl: './clientes.component.html',
  styleUrls: ['./clientes.component.scss']
})
export class ClientesComponent {

  clienteForm: Cliente = {
    nome: '',
    nuit: null,
    bi: null,
    endereco: null,
    telefone: null,
    email: null,
    tipoCliente: 'PESSOA',
    rendaMensal: null
  };

  clientes: Cliente[] = [];
  loading = false;
  errorMessage = '';

  constructor(private clienteService: ClienteService) {}

  listarClientes(): void {
    this.loading = true;
    this.errorMessage = '';

    this.clienteService.listarClientes().subscribe({
      next: (page: PageResponse<Cliente>) => {
        this.clientes = page.content || [];
        this.loading = false;
      },
      error: (err) => {
        console.error(err);
        this.errorMessage = 'Erro ao carregar clientes.';
        this.loading = false;
      }
    });
  }

  criarCliente(): void {
    if (!this.clienteForm.nome) {
      this.errorMessage = 'O nome é obrigatório.';
      return;
    }

    this.loading = true;
    this.errorMessage = '';

    this.clienteService.criarCliente(this.clienteForm).subscribe({
      next: () => {
        // limpar formulário
        this.clienteForm = {
          nome: '',
          nuit: null,
          bi: null,
          endereco: null,
          telefone: null,
          email: null,
          tipoCliente: 'PESSOA',
          rendaMensal: null
        };
        this.listarClientes();
      },
      error: (err) => {
        console.error(err);
        this.errorMessage = 'Erro ao criar cliente.';
        this.loading = false;
      }
    });
  }
}
