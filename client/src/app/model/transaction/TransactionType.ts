class TransactionTypeDef {
  TRANSACTION_AUTHORIZE: Item = {name: 'Authorize'};
  TRANSACTION_CHARGE: Item = {name: 'Charge'};
  TRANSACTION_REFUND: Item = {name: 'Refund'};
  TRANSACTION_REVERSAL: Item = {name: 'Reversal'};
}

export type TransactionType = keyof TransactionTypeDef;
export const transactionType = new TransactionTypeDef();
export const transactionTypes = Object.keys(transactionType) as TransactionType[];

interface Item {
  name: string;
}
