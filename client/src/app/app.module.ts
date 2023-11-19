import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {ErrorHandler, NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {JwtInterceptor} from 'src/app/core/proxy/interceptors/jwt.interceptor';
import {GlobalErrorHandler} from 'src/app/core/services/system/global-error-handler';
import {RoutingModule} from 'src/app/routing/routing.module';
import {SharedModule} from 'src/app/shared/shared/shared.module';

import {AppComponent} from './app.component';

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    RoutingModule,
    BrowserModule,
    HttpClientModule,
    SharedModule,
  ],
  providers: [
    {provide: ErrorHandler, useClass: GlobalErrorHandler},
    {provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true}
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
