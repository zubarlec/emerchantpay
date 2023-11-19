import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {EditMerchantComponent} from 'src/app/modules/admin/pages/edit-merchant/edit-merchant.component';
import {MerchantsComponent} from 'src/app/modules/admin/pages/merchants/merchants.component';
import {AuthGuard} from 'src/app/routing/guards/auth.guard';
import {SharedModule} from 'src/app/shared/shared/shared.module';

const routes: Routes = [
  {
    path: '',
    data: {
      isAvailable: AuthGuard.hasRole('ROLE_ADMIN'),
    },
    children: [
      {
        path: '', redirectTo: 'merchants', pathMatch: 'full'
      }, {
        path: 'merchants',
        data: {
          abstract: false
        },
        children: [
          {
            path: '', component: MerchantsComponent
          }, {
            path: ':id', component: EditMerchantComponent
          }
        ]
      }
    ]
  }];

@NgModule({
  declarations: [
    MerchantsComponent,
    EditMerchantComponent
  ],
  imports: [
    SharedModule,
    RouterModule.forChild(routes)
  ]
})
export class AdminModule { }
