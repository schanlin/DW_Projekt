import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class BackendService {
  private baseURL = 'http://alex-x230t:8080';

  constructor(private http: HttpClient) { }

  getAllUser () {
    return this.http.get(this.baseURL+"/user");
  }
}
