import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import { Observable, of} from 'rxjs';
import {map, catchError} from 'rxjs/operators';
import {User} from "./models/user.model";

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private baseURL = 'http://localhost:4200/api';

  constructor(private http: HttpClient) { }

  getID(){return 1;}
  /*getID(): Observable<User>{
    const thisUser: Observable<User> = this.http.get<User>(`${this.baseURL}/user/${id}`);
    return thisUser.userID;
  }*/
  getLastname() { return 'Meister';}
  /*getLastname() {
    return this.http.get<User[]>(this.baseURL+"/user");}*/
  getFirstname() {return 'Armin';}
  getRole() { return 'Administrator';}
  getEmailAdress(){return 'admin@emxample.com';}
  getUsername(){return 'MeiArm';}

  getAllUser(): Observable<User[]> {
    return this.http.get<User[]>(this.baseURL+"/user");
  }
}
