class MerchantStatusDef {
  MERCHANT_ACTIVE: Item = {name: 'Active'};
  MERCHANT_INACTIVE: Item = {name: 'Inactive'};
}

export type MerchantStatus = keyof MerchantStatusDef;
export const merchantStatus = new MerchantStatusDef();
export const merchantStatuses = Object.keys(merchantStatus) as MerchantStatus[];

interface Item {
  name: string;
}
