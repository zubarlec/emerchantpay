import {Component, OnInit} from '@angular/core';
import {FormControl, UntypedFormArray, Validators} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {MerchantsProxyService} from 'src/app/core/proxy/merchants-proxy.service';
import {MerchantDTO} from 'src/app/model/account/MerchantDTO';
import {merchantStatus, MerchantStatus, merchantStatuses} from 'src/app/model/account/MerchantStatus';
import {ErrorDTO} from 'src/app/model/ErrorDTO';

@Component({
  templateUrl: './edit-merchant.component.html'
})
export class EditMerchantComponent implements OnInit {

  protected readonly merchantStatuses = merchantStatuses;
  protected readonly merchantStatus = merchantStatus;

  merchant?: MerchantDTO;
  error?: ErrorDTO;

  serverCallInProgress: boolean = false;

  readonly email = new FormControl<string>('', {nonNullable: true, validators: [Validators.required, Validators.email]});
  readonly name = new FormControl<string | undefined>(undefined, {nonNullable: true});
  readonly description = new FormControl<string | undefined>(undefined, {nonNullable: true});
  readonly status = new FormControl<MerchantStatus>('MERCHANT_ACTIVE', {nonNullable: true, validators: Validators.required});
  readonly password = new FormControl<string | undefined>(undefined, {nonNullable: true});

  readonly allControls = new UntypedFormArray([this.email, this.name, this.description, this.status])

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private merchantsProxy: MerchantsProxyService
  ) {
  }

  ngOnInit(): void {
    const id = parseInt(this.route.snapshot.params['id']);

    if (id) {
      this.merchantsProxy.getOne(id).subscribe({
        next: result => {
          this.merchant = result;

          this.email.setValue(this.merchant?.email);
          this.name.setValue(this.merchant?.name);
          this.description.setValue(this.merchant?.description);
          this.status.setValue(this.merchant?.status);
        }, error: error => this.error = error.error
      })
    }
  }

  save(): void {
    this.error = undefined;
    if (this.allControls.invalid) {
      this.allControls.controls.forEach(c => c.markAsTouched());
      return;
    }

    this.serverCallInProgress = true;
    const methodActual = this.merchant?.id ? this.merchantsProxy.updateBind : this.merchantsProxy.createBind;
    methodActual({
      merchant: {
        ...this.merchant,
        email: this.email.value,
        name: this.name.value,
        description: this.description.value,
        status: this.status.value
      },
      password: this.password.value
    }).subscribe({next: () => {
      this.serverCallInProgress = false;
      this.router.navigate(['/admin/merchants']).then(() => {});
    }, error: error => {
      this.serverCallInProgress = false;
      this.error = error.error;
    }});
  }

  delete(): void {
    if (!this.merchant?.id) {
      return;
    }
    this.merchantsProxy.delete(this.merchant?.id).subscribe({
      next: () => {
        this.serverCallInProgress = false;
        this.router.navigate(['/admin/merchants']).then(() => {});
      }, error: error => {
        this.serverCallInProgress = false;
        this.error = error.error;
      }
    });
  }
}
