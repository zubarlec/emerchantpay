import {AccountRole} from 'src/app/model/account/AccountRole';

export interface AccountDTO {
  id?: number;
  email: string;
  role?: AccountRole;
}

