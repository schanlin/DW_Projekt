import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Student} from "./models/student.model"
import {Observable} from "rxjs";
import {Classes} from "./models/class.model";

@Injectable({
  providedIn: 'root'
})
export class StudentService {
  private baseURL = '';
  constructor(private http: HttpClient) { }

  getAllStudents(): Observable<Student[]> {
    return this.http.get<Student[]>(this.baseURL+"/student");
  }
}
