import { Component } from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'Biza Microcrédito';

  // secção seleccionada no “menu”
  secao: 'clientes' | 'creditos' | 'pagamentos' = 'clientes';

  seleccionar(secao: 'clientes' | 'creditos' | 'pagamentos') {
    this.secao = secao;
  }
}
