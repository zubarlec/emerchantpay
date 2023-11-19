import {NgModule} from '@angular/core';
import {LoginViewComponent} from 'src/app/modules/login/pages/login-view/login-view.component';
import {LogoutComponent} from 'src/app/modules/login/pages/logout/logout.component';
import {OfflineComponent} from 'src/app/modules/login/pages/offline/offline.component';
import {PageNotFoundComponent} from 'src/app/modules/login/pages/page-not-found/page-not-found.component';
import {SharedModule} from 'src/app/shared/shared/shared.module';

@NgModule({
  declarations: [
    LoginViewComponent,
    LogoutComponent,
    OfflineComponent,
    PageNotFoundComponent
  ],
  imports: [
    SharedModule
  ]
})
export class LoginModule { }
