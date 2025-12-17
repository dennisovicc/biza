import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';

import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';

import { ClientesComponent } from './clientes/clientes.component';
import { CreditosComponent } from './creditos/creditos.component';
import { PagamentosComponent } from './pagamentos/pagamentos.component';
import { CreditoAcoesComponent } from './creditos/credito-acoes/credito-acoes.component';
import { CreditoAprovacaoComponent } from './creditos/credito-aprovacao/credito-aprovacao.component';

@NgModule({
  declarations: [
    AppComponent,
    ClientesComponent,
    CreditosComponent,
    PagamentosComponent,
    CreditoAcoesComponent,
    CreditoAprovacaoComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {}
