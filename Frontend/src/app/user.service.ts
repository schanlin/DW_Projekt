import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import { Observable, of} from 'rxjs';
import {map, catchError} from 'rxjs/operators';
import {User} from "./models/user.model";

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private baseURL = '';

  constructor(private http: HttpClient) { }

  /*getID(){return 1;}
  getLastname() { return 'Meister';}
  getFirstname() {return 'Armin';}
  getRole() { return 'Administrator';}
  getEmailAdress(){return 'admin@emxample.com';}
  getUsername(){return 'MeiArm';}*/

  getAllUser(): Observable<User[]> {
    return this.http.get<User[]>(this.baseURL+"/user");
  }

  onEditUser(editUser:User){
    return this.http.put<User>(this.baseURL+"/user/"+editUser.userID, editUser);
  }

  onDeleteUser(id:number): Observable<boolean>{
    return this.http.delete(this.baseURL+"/user/"+id).pipe(
      map((result) => {return true}),
      catchError((error) => {return of(false)})
    );
  }

  getUserByID(id:number){
    return this.http.get<User>(this.baseURL+"/user/"+id);
  }
  getCurrentUser(){
    return this.http.get<User>(this.baseURL+"/user/current");
  }

  getUserFullNameByID(id:number){
    const user: Observable<User> = this.getUserByID(id);
    const userFullName: Observable<string> = user.pipe(
      map((value) => {
        return value.firstname + ' ' + value.lastname;
      })
    );
   return userFullName;
  }

  addNewUser(newUser: Omit<User, 'userID'>){
    return this.http.post<User>(this.baseURL+"/user", newUser);
  }
}
