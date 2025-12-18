import { Component } from '@angular/core';
import { Router } from '@angular/router';
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
    if (!this.username || !this.password) {
      this.errorMessage = 'Preencha username e password.';
      return;
    }

    this.loading = true;

    this.auth.login({ username: this.username, password: this.password }).subscribe({
      next: (res) => {
        // redireciona por perfil
        if (res.role === 'GESTOR_CREDITO') this.router.navigate(['/credito-aprovacao']);
        else if (res.role === 'ADMIN') this.router.navigate(['/admin']);
        else this.router.navigate(['/clientes']);

        this.loading = false;
      },
      error: (err) => {
        console.error(err);
        this.errorMessage = 'Login inválido.';
        this.loading = false;
      }
    });
  }
}
