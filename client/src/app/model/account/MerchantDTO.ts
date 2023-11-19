import {AccountDTO} from 'src/app/model/account/AccountDTO';
import {MerchantStatus} from 'src/app/model/account/MerchantStatus';

export interface MerchantDTO extends AccountDTO {
  name?: string;
  description?: string;
  status: MerchantStatus;
  totalTransactionSum?: number;
}

