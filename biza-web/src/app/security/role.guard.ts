import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Injectable({ providedIn: 'root' })
export class RoleGuard implements CanActivate {

  constructor(private auth: AuthService, private router: Router) {}

canActivate(route: ActivatedRouteSnapshot): boolean {
  const allowedRoles = route.data['roles'] as string[] | undefined;

  if (!this.auth.isLoggedIn()) {
    this.router.navigate(['/login']);
    return false;
  }

  if (!allowedRoles || allowedRoles.length === 0) return true;

  if (this.auth.hasAnyRole(allowedRoles)) return true;

  const role = this.auth.getUser()?.role;
  const home =
    role === 'ADMIN' ? '/admin'
    : role === 'GESTOR_CREDITO' ? '/credito-aprovacao'
    : '/creditos';

  this.router.navigate([home]);
  return false;
}
}
