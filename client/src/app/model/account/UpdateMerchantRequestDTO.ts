import {MerchantDTO} from 'src/app/model/account/MerchantDTO';

export interface UpdateMerchantRequestDTO {
  merchant: MerchantDTO;
  password?: string;
}

