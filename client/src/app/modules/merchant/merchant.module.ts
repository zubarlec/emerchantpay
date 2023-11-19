import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {TransactionComponent} from 'src/app/modules/merchant/pages/transaction/transaction.component';
import {AuthGuard} from 'src/app/routing/guards/auth.guard';
import {SharedModule} from 'src/app/shared/shared/shared.module';

const routes: Routes = [
  {
    path: '', component: TransactionComponent,
    data: {
      abstract: false,
      isAvailable: AuthGuard.hasAnyRole(['ROLE_ADMIN', 'ROLE_MERCHANT']),
    }
  }];

@NgModule({
  declarations: [
    TransactionComponent
  ],
  imports: [
    SharedModule,
    RouterModule.forChild(routes)
  ]
})
export class MerchantModule { }
