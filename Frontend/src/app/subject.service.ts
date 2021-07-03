import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Subject} from "./models/subject.model"
import {Observable, of} from 'rxjs';
import {User} from "./models/user.model";
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

  deleteSubject(id:number){
    //TODO Request wird nicht gesendet
    return this.http.delete(this.baseURL+"/subject/"+id);
  }

  addNewSubject(newSubject: Omit<Subject, 'subjectID'| 'teacherName' | 'className'>){
    return this.http.post<Subject>(this.baseURL+"/subject", newSubject);
  }

  archiveSubject(subject: Omit<Subject, 'teacherName' | 'className'>){
    return this.http.put(this.baseURL+"/subject", subject);
  }

  editSubject(editSubject: Subject){
    return this.http.put(this.baseURL+"/subject/" + editSubject.subjectID, editSubject);
  }
}
