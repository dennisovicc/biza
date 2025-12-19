import { Component, OnInit } from '@angular/core';
import { CreditoService, CriarCreditoRequest } from '../services/credito.service';
import { PageResponse } from '../models/page-response.model';
import { ClienteService } from '../services/cliente.service';
import { Credito } from '../models/credito.model';
import { Cliente } from '../models/cliente.model';

@Component({
  selector: 'app-creditos',
  templateUrl: './creditos.component.html',
  styleUrls: ['./creditos.component.scss']
})
export class CreditosComponent implements OnInit {

  clientes: Cliente[] = [];
  creditos: Credito[] = [];

  selectedClienteId: number | null = null;

  tipoCredito: 'RAPIDO' | 'LONGO' = 'RAPIDO';
  montante: number | null = null;
  prazoMeses: number | null = 1;

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
    if (this.selectedClienteId == null) {
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
    if (this.selectedClienteId == null) {
      this.errorMessage = 'Seleccione um cliente.';
      return;
    }
    if (this.montante == null || this.montante <= 0) {
      this.errorMessage = 'Informe um montante válido.';
      return;
    }
    if (this.prazoMeses == null || this.prazoMeses <= 0) {
      this.errorMessage = 'Informe um prazo em meses.';
      return;
    }

    const request: CriarCreditoRequest = {
      clienteId: this.selectedClienteId,
      tipoCredito: this.tipoCredito,
      montante: this.montante,
      prazoMeses: this.prazoMeses
    };

    this.loading = true;
    this.errorMessage = '';

    this.creditoService.criar(request).subscribe({
      next: () => {
        this.montante = null;
        this.prazoMeses = 1;
        this.listarCreditos();
      },
      error: (err) => {
        console.error(err);
        this.errorMessage = 'Erro ao criar crédito.';
        this.loading = false;
      }
    });
  }

  // recebe actualizações do componente de acções
  onCreditoUpdated(updated: Credito): void {
    const idx = this.creditos.findIndex(c => c.id === updated.id);
    if (idx >= 0) {
      this.creditos[idx] = updated;
    }
  }
}
