import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {ApiCallService} from 'src/app/core/proxy/api-call.service';
import {ListWrapper} from 'src/app/model/ListWrapper';
import {TransactionDTO} from 'src/app/model/transaction/TransactionDTO';

@Injectable({
  providedIn: 'root'
})
export class TransactionProxyService {

  constructor(
    private apiCall: ApiCallService
  ) {}

  getAll(): Observable<ListWrapper<TransactionDTO>> {
    return this.apiCall.get('transaction/');
  }

  submitTransaction(transaction: TransactionDTO): Observable<TransactionDTO> {
    return this.apiCall.post('transaction/', transaction);
  }
}
