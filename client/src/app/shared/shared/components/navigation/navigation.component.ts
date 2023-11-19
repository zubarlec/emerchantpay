import {Component} from '@angular/core';
import {AuthService} from 'src/app/core/services/auth.service';

@Component({
  selector: 'navigation',
  templateUrl: './navigation.component.html'
})
export class NavigationComponent {

  constructor(
    public authService: AuthService
  ) {
  }

}
