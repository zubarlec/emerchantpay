import {Component, OnInit} from '@angular/core';
import {FormControl, UntypedFormArray, Validators} from '@angular/forms';
import {TransactionProxyService} from 'src/app/core/proxy/transaction-proxy.service';
import {AuthService} from 'src/app/core/services/auth.service';
import {ErrorDTO} from 'src/app/model/ErrorDTO';
import {TransactionDTO} from 'src/app/model/transaction/TransactionDTO';
import {transactionStatus} from 'src/app/model/transaction/TransactionStatus';
import {TransactionType, transactionType, transactionTypes} from 'src/app/model/transaction/TransactionType';

@Component({
  templateUrl: './transaction.component.html'
})
export class TransactionComponent implements OnInit {

  protected readonly transactionStatus = transactionStatus;
  protected readonly transactionType = transactionType;
  protected readonly transactionTypes = transactionTypes;

  transactions?: TransactionDTO[];
  error?: ErrorDTO;

  serverCallInProgress: boolean = false;

  readonly customerEmail = new FormControl<string | undefined>(undefined, {nonNullable: true, validators: Validators.email});
  readonly customerPhone = new FormControl<string | undefined>(undefined, {nonNullable: true});
  readonly referenceId = new FormControl<number | undefined>(undefined, {nonNullable: true});
  readonly type = new FormControl<TransactionType>('TRANSACTION_AUTHORIZE', {nonNullable: true, validators: Validators.required});
  readonly amount = new FormControl<number | undefined>(undefined, {nonNullable: true});

  readonly allControls = new UntypedFormArray([this.customerEmail, this.customerPhone, this.referenceId, this.type, this.amount]);

  constructor(
    public authService: AuthService,
    private transactionProxy: TransactionProxyService
  ) {
  }

  ngOnInit(): void {
    this.transactionProxy.getAll().subscribe({next: result => {
      this.transactions = result.value;
    }, error: error => this.error = error.error});
  }

  submitTransaction(): void {
    this.error = undefined;
    if (this.allControls.invalid) {
      this.allControls.controls.forEach(c => c.markAsTouched());
      return;
    }

    this.serverCallInProgress = true;
    this.transactionProxy.submitTransaction({
      status: 'TRANSACTION_APPROVED',
      customerEmail: this.customerEmail.value,
      customerPhone: this.customerPhone.value,
      referenceId: this.referenceId.value,
      type: this.type.value,
      amount: this.amount.value
    }).subscribe({next: result => {
      this.transactions ??= [];
      this.transactions = [result, ...this.transactions];
      this.serverCallInProgress = false;
    }, error: error => {
      this.serverCallInProgress = false;
      this.error = error.error;
    }});
  }

}
