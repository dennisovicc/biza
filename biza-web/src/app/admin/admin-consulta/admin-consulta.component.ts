import { Component } from '@angular/core';
import { finalize } from 'rxjs/operators';

import { CreditoService } from '../../services/credito.service';
import { PagamentoService } from '../../services/pagamento.service';

import { Credito } from '../../models/credito.model';
import { Pagamento } from '../../models/pagamento.model';
import { PageResponse } from '../../models/page-response.model';

@Component({
  selector: 'app-admin-consulta',
  templateUrl: './admin-consulta.component.html',
  styleUrls: ['./admin-consulta.component.scss']
})
export class AdminConsultaComponent {

  creditos: Credito[] = [];
  pagamentos: Pagamento[] = [];

  filtroStatus = '';
  filtroCreditoId = '';

  loadingCreditos = false;
  loadingPagamentos = false;

  errorCreditos = '';
  errorPagamentos = '';

  constructor(
    private creditoService: CreditoService,
    private pagamentoService: PagamentoService
  ) {}

  carregarCreditos(): void {
    this.loadingCreditos = true;
    this.errorCreditos = '';

    const status = this.filtroStatus?.trim() || undefined;

    this.creditoService.listar(undefined, status)
      .pipe(finalize(() => (this.loadingCreditos = false)))
      .subscribe({
        next: (res: PageResponse<Credito>) => {
          this.creditos = res.content || [];
        },
        error: (err: any) => {
          console.error(err);
          this.errorCreditos = err?.error?.detail || 'Erro ao carregar créditos.';
        }
      });
  }

  carregarPagamentos(): void {
    this.loadingPagamentos = true;
    this.errorPagamentos = '';

    const creditoId = this.filtroCreditoId?.trim() || undefined;

    this.pagamentoService.listar(creditoId)
      .pipe(finalize(() => (this.loadingPagamentos = false)))
      .subscribe({
        next: (res: PageResponse<Pagamento>) => {
          this.pagamentos = res.content || [];
        },
        error: (err: any) => {
          console.error(err);
          this.errorPagamentos = err?.error?.detail || 'Erro ao carregar pagamentos.';
        }
      });
  }
}
