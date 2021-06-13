import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  constructor() { }
  getName() { return 'Armin Meister';}
  getRole() { return 'Administrator';}
  getEmailAdress(){return 'admin@emxample.com'}
}
