import { Component, OnInit } from '@angular/core';
import {UserService} from "../user.service";
import {BackendService} from "../backend.service";


@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css']
})
export class UserComponent implements OnInit {

  constructor(private backendService: BackendService) { }

  //users: ???[] = [];

  ngOnInit(): void {
    //obs.subscribe(e => this.users = e);
  }

}
