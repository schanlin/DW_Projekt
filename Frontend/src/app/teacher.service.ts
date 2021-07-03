import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import { Observable, of} from 'rxjs';
import {map, catchError} from 'rxjs/operators';
import {Teacher} from "./models/teacher.model"

@Injectable({
  providedIn: 'root'
})
export class TeacherService {
  private baseURL = 'http://localhost:4200/api';
  constructor(private http: HttpClient) {  }

  getallTeacher(){
    return this.http.get<Teacher[]>(this.baseURL+"/teacher");
  }
}
