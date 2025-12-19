import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { ClientesComponent } from './clientes/clientes.component';
import { CreditosComponent } from './creditos/creditos.component';
import { PagamentosComponent } from './pagamentos/pagamentos.component';
import { CreditoAprovacaoComponent } from './creditos/credito-aprovacao/credito-aprovacao.component';
import { LoginComponent } from './auth/login/login.component';
import { RoleGuard } from './security/role.guard';
import { AuthGuard } from './security/auth.guard';
import { AdminComponent } from './admin/admin.component';

const routes: Routes = [
  { path: 'login', component: LoginComponent },

  // OFICIAL: clientes + criar crédito
  { path: 'clientes', component: ClientesComponent, canActivate: [AuthGuard, RoleGuard], data: { roles: ['OFICIAL_CREDITO'] } },
  { path: 'creditos', component: CreditosComponent, canActivate: [AuthGuard, RoleGuard], data: { roles: ['OFICIAL_CREDITO'] } },

  // GESTOR: aprovação + pagamentos
  { path: 'credito-aprovacao', component: CreditoAprovacaoComponent, canActivate: [AuthGuard, RoleGuard], data: { roles: ['GESTOR_CREDITO'] } },
  { path: 'pagamentos', component: PagamentosComponent, canActivate: [AuthGuard, RoleGuard], data: { roles: ['GESTOR_CREDITO'] } },

    // ADMIN (tudo dentro)
  { path: 'admin', component: AdminComponent, canActivate: [RoleGuard], data: { roles: ['ADMIN'] } },

  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: '**', redirectTo: 'login' },

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}
