export interface ICurrency {
  currencyID?: string;
  currencyName?: string;
  unitsPerCad?: number;
  cadPerUnit?: number;
  transactionFee?: number;
}

export const defaultValue: Readonly<ICurrency> = {};
