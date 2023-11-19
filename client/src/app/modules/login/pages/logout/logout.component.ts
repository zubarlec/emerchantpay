import {Component, OnInit} from '@angular/core';
import {AuthService} from 'src/app/core/services/auth.service';

@Component({
  templateUrl: './logout.component.html'
})
export class LogoutComponent implements OnInit {

  constructor(
    private authService: AuthService
  ) {
  }

  ngOnInit(): void {
    this.authService.logout();
  }

}
