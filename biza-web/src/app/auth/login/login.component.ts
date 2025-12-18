import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { finalize } from 'rxjs/operators';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {
  username = '';
  password = '';

  loading = false;
  errorMessage = '';

  constructor(private auth: AuthService, private router: Router) {}

  submit(): void {
    this.errorMessage = '';
    this.loading = true;

    this.auth.login({ username: this.username, password: this.password })
      .pipe(finalize(() => (this.loading = false)))
      .subscribe({
        next: (res) => {
          // guarda token + user
          this.auth.setSession(res);

          // redirect por perfil
          const role = res.user?.role;
          const target =
            role === 'GESTOR_CREDITO' ? '/credito-aprovacao'
            : role === 'ADMIN'        ? '/clientes'
            : '/creditos'; // OFICIAL_CREDITO

          this.router.navigateByUrl(target);
        },
        error: (err) => {
          console.error(err);
          this.errorMessage = err?.error?.detail || 'Falha no login.';
        }
      });
  }
}
