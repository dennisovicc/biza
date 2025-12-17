import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Credito } from '../../models/credito.model';
import { CreditoService } from '../../services/credito.service';

@Component({
  selector: 'app-credito-acoes',
  templateUrl: './credito-acoes.component.html',
  styleUrls: ['./credito-acoes.component.scss']
})
export class CreditoAcoesComponent {

  @Input() credito!: Credito;                       // crédito da linha
  @Output() updated = new EventEmitter<Credito>();  // emite quando o crédito muda (estado, saldo, etc.)

  loading = false;
  errorMessage = '';

  constructor(private creditoService: CreditoService) {}

  aprovar(): void {
    this.loading = true;
    this.errorMessage = '';
    this.creditoService.aprovar(this.credito.id).subscribe({
      next: (c: Credito) => this.updated.emit(c),
      error: (err: any) => {
        console.error(err);
        this.errorMessage = 'Erro ao aprovar.';
      },
      complete: () => this.loading = false
    });
  }

  rejeitar(): void {
    this.loading = true;
    this.errorMessage = '';
    this.creditoService.rejeitar(this.credito.id).subscribe({
      next: (c: Credito) => this.updated.emit(c),
      error: (err: any) => {
        console.error(err);
        this.errorMessage = 'Erro ao rejeitar.';
      },
      complete: () => this.loading = false
    });
  }

  liberar(): void {
    this.loading = true;
    this.errorMessage = '';
    this.creditoService.liberar(this.credito.id).subscribe({
      next: (c: Credito) => this.updated.emit(c),
      error: (err: any) => {
        console.error(err);
        this.errorMessage = 'Erro ao liberar.';
      },
      complete: () => this.loading = false
    });
  }

  atualizarSaldo(): void {
    this.loading = true;
    this.errorMessage = '';
    this.creditoService.atualizarSaldo(this.credito.id).subscribe({
      next: (c: Credito) => this.updated.emit(c),
      error: (err: any) => {
        console.error(err);
        this.errorMessage = 'Erro ao actualizar saldo.';
      },
      complete: () => this.loading = false
    });
  }

  liquidar(): void {
    this.loading = true;
    this.errorMessage = '';
    this.creditoService.liquidar(this.credito.id).subscribe({
      next: (c: Credito) => this.updated.emit(c),
      error: (err: any) => {
        console.error(err);
        this.errorMessage = 'Erro ao liquidar.';
      },
      complete: () => this.loading = false
    });
  }
}
