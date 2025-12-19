import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';

import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';

import { ClientesComponent } from './clientes/clientes.component';
import { CreditosComponent } from './creditos/creditos.component';
import { PagamentosComponent } from './pagamentos/pagamentos.component';
import { CreditoAcoesComponent } from './creditos/credito-acoes/credito-acoes.component';
import { CreditoAprovacaoComponent } from './creditos/credito-aprovacao/credito-aprovacao.component';
import { AuthInterceptor } from './security/auth.interceptor';
import { LoginComponent } from './auth/login/login.component';
import { AdminUtilizadoresComponent } from './admin/admin-utilizadores/admin-utilizadores.component';
import { AdminClientesComponent } from './admin/admin-clientes/admin-clientes.component';
import { AdminConsultaComponent } from './admin/admin-consulta/admin-consulta.component';
import { AdminComponent } from './admin/admin.component';

@NgModule({
  declarations: [
    AppComponent,
    ClientesComponent,
    CreditosComponent,
    PagamentosComponent,
    CreditoAcoesComponent,
    CreditoAprovacaoComponent,
    LoginComponent,
    AdminUtilizadoresComponent,
    AdminClientesComponent,
    AdminConsultaComponent,
    AdminComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule
  ],
  providers: [
        { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true }
  ],
  bootstrap: [AppComponent]
})
export class AppModule {}
