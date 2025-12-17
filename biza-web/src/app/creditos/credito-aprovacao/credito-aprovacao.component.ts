import { Component, OnInit } from '@angular/core';
import { Credito } from '../../models/credito.model';
import { CreditoService, PageResponse } from '../../services/credito.service';

@Component({
  selector: 'app-credito-aprovacao',
  templateUrl: './credito-aprovacao.component.html',
  styleUrls: ['./credito-aprovacao.component.scss']
})
export class CreditoAprovacaoComponent implements OnInit {

  creditos: Credito[] = [];
  loading = false;
  errorMessage = '';

  // filtro padrão: ver só pedidos para aprovar/rejeitar
  filtroStatus: 'SOLICITADO' | 'APROVADO' | 'EM_CURSO' | 'REJEITADO' | 'LIQUIDADO' = 'SOLICITADO';

  constructor(private creditoService: CreditoService) {}

  ngOnInit(): void {
    this.carregar();
  }

  carregar(): void {
    this.loading = true;
    this.errorMessage = '';

    this.creditoService.listar(undefined, this.filtroStatus).subscribe({
      next: (page: PageResponse<Credito>) => {
        this.creditos = page.content || [];
        this.loading = false;
      },
      error: (err: any) => {
        console.error(err);
        this.errorMessage = 'Erro ao carregar créditos para aprovação.';
        this.loading = false;
      }
    });
  }

  onCreditoUpdated(updated: Credito): void {
    const idx = this.creditos.findIndex(c => c.id === updated.id);
    if (idx >= 0) {
      this.creditos[idx] = updated;
    } else {
      // se não existir (caso raro), recarrega para garantir consistência
      this.carregar();
    }
  }

  onFiltroChange(status: any): void {
    this.filtroStatus = status;
    this.carregar();
  }
}
