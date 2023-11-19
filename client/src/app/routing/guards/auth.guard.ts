import {inject, Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivateFn, Router, RouterStateSnapshot, UrlTree} from '@angular/router';
import {catchError, map, Observable, of} from 'rxjs';
import {AuthService} from 'src/app/core/services/auth.service';
import {AccountRole} from 'src/app/model/account/AccountRole';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard {
  private static instance: AuthGuard;

  constructor(
    private authService: AuthService,
    private router: Router
  ) {
    AuthGuard.instance = this;
  }
  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    if (!this.authService.isAuthenticationStateSynced) {
      return this.authService.reloadAuth().pipe(
        map(() => this.router.parseUrl(state.url)),
        catchError(() => of(this.router.createUrlTree(['/offline'])))
      );
    }

    const isAvailable = !route.data?.['isAvailable'] || route.data?.['isAvailable']();
    if (!isAvailable) {
      if (this.authService.isAuthenticated) {
        return this.router.createUrlTree([this.authService.homeState]);
      } else {
        return this.router.createUrlTree(['/']);
      }
    }

    return true;
  }

  static authenticated(): boolean {
    return AuthGuard.instance.authService.isAuthenticated;
  }

  static notAuthenticated(): boolean {
    return !AuthGuard.authenticated();
  }

  static hasRole(role: AccountRole): () => boolean {
    return () => AuthGuard.instance.authService.hasAnyRole([role]);
  }

  static hasAnyRole(roles: AccountRole[]): () => boolean {
    return () => AuthGuard.instance.authService.hasAnyRole(roles);
  }
}

export const canActivate: CanActivateFn = (route, state) => inject(AuthGuard).canActivate(route, state);
