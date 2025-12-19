import { Component } from '@angular/core';

type AdminTab = 'utilizadores' | 'clientes' | 'consulta';

@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.scss']
})
export class AdminComponent {
  tab: AdminTab = 'utilizadores';

  setTab(t: AdminTab) {
    this.tab = t;
  }
}
