class TransactionStatusDef {
  TRANSACTION_APPROVED: Item = {name: 'Approved'};
  TRANSACTION_REVERSED: Item = {name: 'Reversed'};
  TRANSACTION_REFUNDED: Item = {name: 'Refunded'};
  TRANSACTION_ERROR: Item = {name: 'Error'};
}

export type TransactionStatus = keyof TransactionStatusDef;
export const transactionStatus = new TransactionStatusDef();
export const transactionStatuses = Object.keys(transactionStatus) as TransactionStatus[];

interface Item {
  name: string;
}

