import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {LoginModule} from 'src/app/modules/login/login.module';
import {LoginViewComponent} from 'src/app/modules/login/pages/login-view/login-view.component';
import {LogoutComponent} from 'src/app/modules/login/pages/logout/logout.component';
import {OfflineComponent} from 'src/app/modules/login/pages/offline/offline.component';
import {PageNotFoundComponent} from 'src/app/modules/login/pages/page-not-found/page-not-found.component';
import {AuthGuard, canActivate} from 'src/app/routing/guards/auth.guard';

const routes: Routes = [{
  path: '',
  data: {
    abstract: true
  },
  canActivate: [canActivate],
  canActivateChild: [canActivate],
  children: [
    {
      path: '', component: LoginViewComponent,
      data: {
        abstract: false,
        isAvailable: AuthGuard.notAuthenticated,
        isLoginState: true,
      }
    }, {
      path: 'logout', component: LogoutComponent,
      data: {
        abstract: false,
        isLoginState: true,
      }
    }, {
      path: 'admin', loadChildren: () => import('../modules/admin/admin.module').then(m => m.AdminModule)
    }, {
      path: 'merchant', loadChildren: () => import('../modules/merchant/merchant.module').then(m => m.MerchantModule)
    }, {
      path: 'offline', component: OfflineComponent,
      data: {
        abstract: false
      }
    }, {
      path: '**', component: PageNotFoundComponent,
      data: {
        abstract: false
      }
    }
  ]
}];

@NgModule({
  imports: [
    RouterModule.forRoot(routes, { enableTracing: false, paramsInheritanceStrategy: 'always' } ),
    LoginModule
  ],
  exports: [RouterModule]
})
export class RoutingModule { }
