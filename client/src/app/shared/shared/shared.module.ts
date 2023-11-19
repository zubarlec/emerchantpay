import {CommonModule} from '@angular/common';
import {NgModule} from '@angular/core';
import {ReactiveFormsModule} from '@angular/forms';
import {RouterModule} from '@angular/router';
import {NavigationComponent} from 'src/app/shared/shared/components/navigation/navigation.component';

@NgModule({
  declarations: [
    NavigationComponent
  ],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterModule
  ],
  exports: [
    CommonModule,
    ReactiveFormsModule,
    RouterModule,
    NavigationComponent
  ]
})
export class SharedModule { }
