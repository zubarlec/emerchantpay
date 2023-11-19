import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {ApiCallService} from 'src/app/core/proxy/api-call.service';
import {MerchantDTO} from 'src/app/model/account/MerchantDTO';
import {UpdateMerchantRequestDTO} from 'src/app/model/account/UpdateMerchantRequestDTO';
import {ListWrapper} from 'src/app/model/ListWrapper';
import {ValueWrapper} from 'src/app/model/ValueWrapper';

@Injectable({
  providedIn: 'root'
})
export class MerchantsProxyService {

  constructor(
    private apiCall: ApiCallService
  ) {}

  getAll(): Observable<ListWrapper<MerchantDTO>> {
    return this.apiCall.get('merchants/');
  }

  getOne(id: number): Observable<MerchantDTO> {
    return this.apiCall.get('merchants/' + id);
  }

  createOrUpdate(updateRequest: UpdateMerchantRequestDTO): Observable<MerchantDTO> {
    return this.apiCall.post('merchants/', updateRequest);
  }

  delete(id: number): Observable<ValueWrapper<boolean>> {
    return this.apiCall.delete('merchants/' + id)
  }
}
