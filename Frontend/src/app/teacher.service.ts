import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Teacher} from "./models/teacher.model"
import {Subject} from "./models/subject.model";
import {TestResult} from "./models/test-result.model";
import {Test} from "./models/test.model";

@Injectable({
  providedIn: 'root'
})
export class TeacherService {
  private baseURL = 'http://localhost:4200/api';
  constructor(private http: HttpClient) {  }

  getAllTeacher(){
    return this.http.get<Teacher[]>(this.baseURL+"/teacher");
  }

  addTestResult(result: TestResult){
    return this.http.post(this.baseURL+"/teacher/tests/results", result);
  }

  getAllSubjectsForTeacher(teacherID:number){
    return this.http.get<Subject[]>(this.baseURL+"/teacher/"+teacherID+"/subjects");
  }

  getAllTestsForSubject(subjectID:number){
    return this.http.get<Test[]>(this.baseURL+"/teacher/subjects/"+subjectID+"/tests");
  }

  getAllResultsForTest(testID:number){
    return this.http.get<TestResult[]>(this.baseURL+"/teacher/tests/"+testID+"/results");
  }
}
