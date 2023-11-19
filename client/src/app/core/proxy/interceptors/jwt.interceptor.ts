import {HttpEvent, HttpHandler, HttpHeaders, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {RootScopeService} from 'src/app/root-scope.service';

const AUTH_HEADER = 'Authorization';
@Injectable()
export class JwtInterceptor implements HttpInterceptor {
  constructor(
    private rootScope: RootScopeService,
  ) {}

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    if (!request.url.startsWith(this.rootScope.apiBaseUrl) || !this.rootScope.jwt) {
      return next.handle(request);
    }

    let headers: HttpHeaders = request.headers.set(AUTH_HEADER, `Bearer ${this.rootScope.jwt}`);

    return next.handle(request.clone({headers}));
  }
}
