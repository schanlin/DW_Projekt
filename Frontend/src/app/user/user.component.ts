import { Component, OnInit } from '@angular/core';
import {BackendService} from "../backend.service";
import {User} from "../models/user.model";
import {faPen} from '@fortawesome/free-solid-svg-icons';
import {faTrash} from '@fortawesome/free-solid-svg-icons';



@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css']
})
export class UserComponent implements OnInit {

  constructor(private backendService: BackendService, private userService: UserService) { }
  users: User[] = [];
  readonly faPen = faPen;
  readonly faTrash = faTrash;
  readonly faTimes = faTimes;



  onEditUser(){
    alert("Ist geklickt")
  }


  onDeleteUser(){}

  ngOnInit(): void {
    const obs = this.backendService.getAllUser();
    obs.subscribe(e => this.users = e);

  }

}
