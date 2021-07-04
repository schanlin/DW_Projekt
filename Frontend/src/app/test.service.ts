import { Injectable } from '@angular/core';
import {Test} from "./models/test.model";
import {Subject} from "./models/subject.model";
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class TestService {
  private baseURL = 'http://localhost:4200/api';

  constructor(private http: HttpClient) { }

  addTest(test: Test){
    return this.http.post<Test>(this.baseURL + "/test", test);
  }

  deleteTest(testID: number) {
    return this.http.delete(this.baseURL + "/test/" +testID);
  }
}
