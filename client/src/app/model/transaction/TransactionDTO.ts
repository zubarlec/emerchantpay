import {MerchantDTO} from 'src/app/model/account/MerchantDTO';
import {TransactionStatus} from 'src/app/model/transaction/TransactionStatus';
import {TransactionType} from 'src/app/model/transaction/TransactionType';

export interface TransactionDTO {
  id?: number;
  uuid?: string;
  status: TransactionStatus;
  timestamp?: number;
  customerEmail?: string;
  customerPhone?: string;
  referenceId?: number;
  amount?: number;
  type: TransactionType;
  merchant?: MerchantDTO;
}

