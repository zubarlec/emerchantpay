import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Params} from '@angular/router';
import {Observable} from 'rxjs';
import {RootScopeService} from '../../root-scope.service';

@Injectable({
  providedIn: 'root'
})
export class ApiCallService {

  constructor(
    private http: HttpClient,
    private rootScope: RootScopeService
  ) {
  }

  get<T>(url: string, params?: Params): Observable<T> {
    return this.http.get<T>(this.rootScope.apiBaseUrl + url, {params});
  }

  postForm<T>(url: string, body: {[param: string]: string | number | boolean | readonly (string | number | boolean)[]}): Observable<T> {
    const bodyActual = new HttpParams({fromObject: body});

    const headers = new HttpHeaders({'Content-Type':  'application/x-www-form-urlencoded'});

    return this.http.post<T>(this.rootScope.apiBaseUrl + url, bodyActual, {headers});
  }

  post<T>(url: string, body?: object): Observable<T> {
    const headers = new HttpHeaders({'Content-Type':  'application/json'});

    return this.http.post<T>(this.rootScope.apiBaseUrl + url, body, {headers});
  }

  delete<T>(url: string, body?: object): Observable<T> {
    return this.http.delete<T>(this.rootScope.apiBaseUrl + url, body);
  }

}
