import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {User} from "./models/user.model";

@Injectable({
  providedIn: 'root'
})
export class BackendService {
  private baseURL = 'http://localhost:4200/api';

  // TODO Subject as "Store light"?

  constructor(private http: HttpClient) { }

  getAllUser () {
    return this.http.get<User[]>(this.baseURL+"/user");
  }
}
