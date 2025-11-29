import { Component, OnInit } from '@angular/core';
import { CreditoService, CriarCreditoRequest } from '../services/credito.service';
import { ClienteService } from '../services/cliente.service';
import { Credito } from '../models/credito.model';
import { Cliente } from '../models/cliente.model';

interface PageResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}

@Component({
  selector: 'app-creditos',
  templateUrl: './creditos.component.html',
  styleUrls: ['./creditos.component.scss']
})
export class CreditosComponent implements OnInit {

  clientes: Cliente[] = [];
  creditos: Credito[] = [];

  selectedClienteId: string | null = null;
  montante: number | null = null;
  taxaJurosAnual: number | null = null;
  prazoMeses: number | null = null;

  loading = false;
  errorMessage = '';

  constructor(
    private creditoService: CreditoService,
    private clienteService: ClienteService
  ) {}

  ngOnInit(): void {
    this.carregarClientes();
  }

  carregarClientes(): void {
    this.clienteService.listarClientes().subscribe({
      next: (page: PageResponse<Cliente>) => {
        this.clientes = page.content || [];
      },
      error: (err) => {
        console.error(err);
        this.errorMessage = 'Erro ao carregar clientes.';
      }
    });
  }

  listarCreditos(): void {
    if (!this.selectedClienteId) {
      this.errorMessage = 'Seleccione um cliente para ver os créditos.';
      return;
    }

    this.loading = true;
    this.errorMessage = '';

    this.creditoService.listar(this.selectedClienteId).subscribe({
      next: (page: PageResponse<Credito>) => {
        this.creditos = page.content || [];
        this.loading = false;
      },
      error: (err) => {
        console.error(err);
        this.errorMessage = 'Erro ao carregar créditos.';
        this.loading = false;
      }
    });
  }

  criarCredito(): void {
    if (!this.selectedClienteId) {
      this.errorMessage = 'Seleccione um cliente.';
      return;
    }
    if (this.montante == null || this.montante <= 0) {
      this.errorMessage = 'Informe um montante válido.';
      return;
    }
    if (this.taxaJurosAnual == null || this.taxaJurosAnual < 0) {
      this.errorMessage = 'Informe uma taxa de juros válida.';
      return;
    }
    if (this.prazoMeses == null || this.prazoMeses <= 0) {
      this.errorMessage = 'Informe um prazo em meses.';
      return;
    }

    const request: CriarCreditoRequest = {
      clienteId: this.selectedClienteId,
      montante: this.montante,
      taxaJurosAnual: this.taxaJurosAnual,
      prazoMeses: this.prazoMeses
    };

    this.loading = true;
    this.errorMessage = '';

    this.creditoService.criar(request).subscribe({
      next: () => {
        this.montante = null;
        this.taxaJurosAnual = null;
        this.prazoMeses = null;
        this.listarCreditos();
      },
      error: (err) => {
        console.error(err);
        this.errorMessage = 'Erro ao criar crédito.';
        this.loading = false;
      }
    });
  }
}
