import {Injectable} from '@angular/core';
import {Params, Router} from '@angular/router';
import {catchError, concatMap, Observable, tap} from 'rxjs';
import {LoginProxyService} from 'src/app/core/proxy/login-proxy.service';
import {LocalStorageService} from 'src/app/core/services/system/local-storage.service';
import {AccountDTO} from 'src/app/model/account/AccountDTO';
import {AccountRole} from 'src/app/model/account/AccountRole';
import {RootScopeService} from 'src/app/root-scope.service';

const JWT_KEY = 'jwtValue';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  isAuthenticationStateSynced: boolean = false;
  isAuthenticated: boolean = false;
  currentAccount?: AccountDTO;

  isAdmin: boolean = false;
  isMerchant: boolean = false;

  homeState: string = '/not-allowed';

  constructor(
    private rootScope: RootScopeService,
    private router: Router,
    private loginProxy: LoginProxyService,
    private localStorage: LocalStorageService,
  ) { }

  reloadAuth(): Observable<AccountDTO | undefined> {
    this.rootScope.jwt = this.localStorage.get(JWT_KEY);
    return this.loginProxy.getAuthenticatedAccount().pipe(
      tap(account => this.finalizeLogin(account)),
      catchError(error => {
        this.finalizeLogin(undefined);
        throw error;
      })
    );
  }

  login(username: string, password: string): Observable<AccountDTO> {
    return this.loginProxy.login(username, password).pipe(
      tap(loginResult => {
        this.rootScope.jwt = loginResult.value;
        this.localStorage.put(JWT_KEY, loginResult.value)
      }),
      concatMap(() => this.loginProxy.getAuthenticatedAccount()),
      tap(account => this.finalizeLogin(account))
    );
  }

  logout(): void {
    this.finalizeLogin(undefined);
    this.localStorage.clear();
    this.rootScope.jwt = undefined;
    this.goLoginState();
  }

  private finalizeLogin(account?: AccountDTO): void {
    this.isAuthenticationStateSynced = true;
    this.currentAccount = account;
    this.isAuthenticated = !!this.currentAccount?.id;
    this.isAdmin = this.hasAnyRole(['ROLE_ADMIN']);
    this.isMerchant = this.hasAnyRole(['ROLE_MERCHANT']);

    if (this.isAuthenticated) {
      this.homeState = this.getHomeState();
    }
  }

  private getHomeState(): string {
    if (!this.isAuthenticated) {
      return '/';
    } else if (this.isAdmin) {
      return '/admin';
    } else if (this.isMerchant) {
      return '/merchant';
    } else {
      return '/not-allowed';
    }
  }

  goLoginState(queryParams?: Params): void {
    this.router.navigate(['/'], {queryParams}).then(() => {});
  }

  goHomeState(): void {
    this.router.navigate([this.homeState]).then(() => {});
  }

  hasAnyRole(roles: AccountRole[]): boolean {
    if (!this.isAuthenticated) {
      return false;
    }
    return roles.some(role => this.currentAccount?.role === role);
  }
}
