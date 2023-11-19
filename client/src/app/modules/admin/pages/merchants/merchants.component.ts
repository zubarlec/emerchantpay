import {Component, OnInit} from '@angular/core';
import {MerchantsProxyService} from 'src/app/core/proxy/merchants-proxy.service';
import {MerchantDTO} from 'src/app/model/account/MerchantDTO';
import {merchantStatus} from 'src/app/model/account/MerchantStatus';
import {ErrorDTO} from 'src/app/model/ErrorDTO';

@Component({
  templateUrl: './merchants.component.html'
})
export class MerchantsComponent implements OnInit {

  protected readonly merchantStatus = merchantStatus;

  merchants?: MerchantDTO[];
  error?: ErrorDTO;

  constructor(
    private merchantsProxy: MerchantsProxyService
  ) {
  }

  ngOnInit(): void {
    this.merchantsProxy.getAll().subscribe({next: result => {
      this.merchants = result.value;
    }, error: error => this.error = error.error});
  }
}
