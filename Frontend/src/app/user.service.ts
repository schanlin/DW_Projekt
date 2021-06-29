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
  getLastname() { return 'Meister';}

  getFirstname() {return 'Armin';}
  getRole() { return 'Administrator';}
  getEmailAdress(){return 'admin@emxample.com';}
  getUsername(){return 'MeiArm';}

  getAllUser(): Observable<User[]> {
    return this.http.get<User[]>(this.baseURL+"/user");
  }
}
