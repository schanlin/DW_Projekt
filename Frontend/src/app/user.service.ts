import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  constructor() { }
  getLastname() { return 'Meister';}
  getFirstname() {return 'Armin';}
  getRole() { return 'Administrator';}
  getEmailAdress(){return 'admin@emxample.com';}
  getID(){return 1;}
  getUsername(){return 'MeiArm';}
}
