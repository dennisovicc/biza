import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { ClientesComponent } from './clientes/clientes.component';
import { CreditosComponent } from './creditos/creditos.component';
import { PagamentosComponent } from './pagamentos/pagamentos.component';
import { CreditoAprovacaoComponent } from './creditos/credito-aprovacao/credito-aprovacao.component';

const routes: Routes = [
  { path: 'clientes', component: ClientesComponent },
  { path: 'creditos', component: CreditosComponent },
  { path: 'pagamentos', component: PagamentosComponent },

  // PERFIL APROVADOR
  { path: 'credito-aprovacao', component: CreditoAprovacaoComponent },

  { path: '', redirectTo: 'clientes', pathMatch: 'full' },
  { path: '**', redirectTo: 'clientes' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}
