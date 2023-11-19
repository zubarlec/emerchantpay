import {Injectable} from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class LocalStorageService {
  private readonly localStorageInstance?: Storage;

  constructor() {
    try {
      this.localStorageInstance = window.localStorage;
    } catch (e) {
      this.localStorageInstance = undefined;
    }
  }

  put(key: string, value: any): void {
    if (this.localStorageInstance) {
      if (value === undefined || value === null || Number.isNaN(value)) {
        this.localStorageInstance.removeItem(key);
      } else {
        this.localStorageInstance.setItem(key, typeof value === 'string' ? value : JSON.stringify(value));
      }
    }
  }

  get(key: string): string | undefined {
    if (this.localStorageInstance) {
      return this.localStorageInstance.getItem(key) || undefined;
    }
    return undefined;
  }

  clear(): void {
    if (this.localStorageInstance) {
      this.localStorageInstance.clear();
    }
  }

}
