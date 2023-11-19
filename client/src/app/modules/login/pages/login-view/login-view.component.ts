import {Component} from '@angular/core';
import {FormControl, Validators} from '@angular/forms';
import {AuthService} from 'src/app/core/services/auth.service';
import {ErrorDTO} from 'src/app/model/ErrorDTO';

@Component({
  templateUrl: './login-view.component.html'
})
export class LoginViewComponent {

  serverCallInProgress: boolean = false;
  error?: ErrorDTO;

  readonly username = new FormControl<string>('', {validators: [Validators.required, Validators.email], nonNullable: true});
  readonly password = new FormControl<string>('', {validators: Validators.required, nonNullable: true});

  constructor(
    private authService: AuthService
  ) {
  }
  login(): void {
    if (this.serverCallInProgress || this.username.invalid || this.password.invalid) {
      return;
    }

    this.error = undefined;
    this.serverCallInProgress = true;

    this.authService.login(this.username.value, this.password.value).subscribe({
      next: () => {
        this.serverCallInProgress = false;
        this.authService.goHomeState();
      },
      error: error => {
        this.serverCallInProgress = false;
        this.error = error.error;
      }
    });
  }
}
