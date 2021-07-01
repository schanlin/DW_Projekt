import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Classes} from "./models/class.model"
import {Observable, of} from 'rxjs';
import {User} from "./models/user.model";
import {map} from "rxjs/operators";


@Injectable({
  providedIn: 'root'
})
export class ClassesService {
  private baseURL = 'http://localhost:4200/api';
  constructor(private http: HttpClient) { }

  getAllClasses(): Observable<Classes[]>{
    return this.http.get<Classes[]>(this.baseURL+"/klasse");
  }

  getClassByID(id:number) {
    return this.http.get<Classes>(this.baseURL+"/klasse/"+id);
  }

  addNewClass(newClass: Omit<Classes, 'klassenID'>){
    return this.http.post<Classes>(this.baseURL+"/klasse" , newClass);
  }

  onEditClass(editClass: Classes){
    return this.http.put<Classes>(this.baseURL+"/klasse/" + editClass.klassenID, editClass);
  }

  onDeleteClass(id:number){
    return this.http.delete(this.baseURL+"/klasse/"+id);
  }

}
