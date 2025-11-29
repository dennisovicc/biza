import { Component, OnInit } from '@angular/core';
import { PagamentoService, RegistrarPagamentoRequest } from '../services/pagamento.service';
import { Pagamento } from '../models/pagamento.model';
import { CreditoService } from '../services/credito.service';
import { Credito } from '../models/credito.model';

interface PageResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}

@Component({
  selector: 'app-pagamentos',
  templateUrl: './pagamentos.component.html',
  styleUrls: ['./pagamentos.component.scss']
})
export class PagamentosComponent implements OnInit {

  creditos: Credito[] = [];
  pagamentos: Pagamento[] = [];

  selectedCreditoId: string | null = null;
  valor: number | null = null;

  loading = false;
  errorMessage = '';

  constructor(
    private pagamentoService: PagamentoService,
    private creditoService: CreditoService
  ) {}

  ngOnInit(): void {
    this.carregarCreditos();
  }

  carregarCreditos(): void {
    // lista todos os créditos (sem filtro)
    this.creditoService.listar().subscribe({
      next: (page: PageResponse<Credito>) => {
        this.creditos = page.content || [];
      },
      error: (err) => {
        console.error(err);
        this.errorMessage = 'Erro ao carregar créditos.';
      }
    });
  }

  listarPagamentos(): void {
    if (!this.selectedCreditoId) {
      this.errorMessage = 'Seleccione um crédito para ver os pagamentos.';
      return;
    }

    this.loading = true;
    this.errorMessage = '';

    this.pagamentoService.listar(this.selectedCreditoId).subscribe({
      next: (page: PageResponse<Pagamento>) => {
        this.pagamentos = page.content || [];
        this.loading = false;
      },
      error: (err) => {
        console.error(err);
        this.errorMessage = 'Erro ao carregar pagamentos.';
        this.loading = false;
      }
    });
  }

  registarPagamento(): void {
    if (!this.selectedCreditoId) {
      this.errorMessage = 'Seleccione um crédito.';
      return;
    }
    if (this.valor == null || this.valor <= 0) {
      this.errorMessage = 'Informe um valor de pagamento válido.';
      return;
    }

    const request: RegistrarPagamentoRequest = {
      creditoId: this.selectedCreditoId,
      valor: this.valor,
      dataPagamento: null // backend usa now() se for null
    };

    this.loading = true;
    this.errorMessage = '';

    this.pagamentoService.registrar(request).subscribe({
      next: () => {
        this.valor = null;
        this.listarPagamentos();
      },
      error: (err) => {
        console.error(err);
        this.errorMessage = 'Erro ao registar pagamento.';
        this.loading = false;
      }
    });
  }
}
