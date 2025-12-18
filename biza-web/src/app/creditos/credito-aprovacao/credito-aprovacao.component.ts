import { Component, OnInit } from '@angular/core';
import { CreditoService, PageResponse } from '../../services/credito.service';
import { Credito } from '../../models/credito.model';

@Component({
  selector: 'app-credito-aprovacao',
  templateUrl: './credito-aprovacao.component.html',
  styleUrls: ['./credito-aprovacao.component.scss']
})
export class CreditoAprovacaoComponent implements OnInit {

  creditos: Credito[] = [];
  loading = false;
  errorMessage = '';

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
      // opcional: se mudou de estado e já não pertence ao filtro, recarrega
      if (updated.status !== this.filtroStatus) {
        this.carregar();
      }
    } else {
      this.carregar();
    }
  }
}
