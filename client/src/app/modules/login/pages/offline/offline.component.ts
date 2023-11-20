import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {AuthService} from 'src/app/core/services/auth.service';

@Component({
  templateUrl: './offline.component.html'
})
export class OfflineComponent implements OnInit {
  constructor(
    private authService: AuthService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.authService.reloadAuth().subscribe({
      next: () => this.router.navigate(['/']).then(() => {}), // no longer offline
      error: () => {}
    });
  }
}
