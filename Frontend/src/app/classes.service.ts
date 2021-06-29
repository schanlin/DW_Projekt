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
}
