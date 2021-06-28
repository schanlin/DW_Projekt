import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable, of} from 'rxjs';
import {User} from "./models/user.model";

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private baseURL = 'http://localhost:4200/api';

  constructor(private http: HttpClient) { }
  getLastname() { return 'Meister';}
  /*getLastname() {
    return this.http.get<User[]>(this.baseURL+"/user");}*/
  getFirstname() {return 'Armin';}
  getRole() { return 'Administrator';}
  getEmailAdress(){return 'admin@emxample.com';}
  getID(){return 1;}
  getUsername(){return 'MeiArm';}
}
