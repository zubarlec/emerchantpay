import {Injectable} from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class RootScopeService {
  apiBaseUrl: string = '/api/v1/'
  jwt?: string;
}
