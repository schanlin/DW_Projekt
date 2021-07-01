import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Subject} from "./models/subject.model"
import {Observable, of} from 'rxjs';
// Fontawesome

@Injectable({
  providedIn: 'root'
})
export class SubjectService {
  private baseURL = 'http://localhost:4200/api';

  constructor(private http: HttpClient) { }

  getAllSubjects(): Observable<Subject[]>{
    return this.http.get<Subject[]>(this.baseURL+"/subject");
  }

  getSubjectyID(id:number){
    return this.http.get<Subject>(this.baseURL+"/subject/"+id);
  }
}
