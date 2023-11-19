import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {AccountDTO} from 'src/app/model/account/AccountDTO';
import {ValueWrapper} from 'src/app/model/ValueWrapper';
import {ApiCallService} from './api-call.service';

@Injectable({
  providedIn: 'root'
})
export class LoginProxyService {

  constructor(
    private apiCall: ApiCallService
  ) {}

  login(username: string, password: string): Observable<ValueWrapper<string>> {
    return this.apiCall.postForm('auth/login', {username, password});
  }

  getAuthenticatedAccount(): Observable<AccountDTO> {
    return this.apiCall.get('auth/account');
  }
}
