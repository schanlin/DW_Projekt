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

  constructor(private userService: UserService) { }
  users: User[] = [];
  readonly faPen = faPen;
  readonly faTrash = faTrash;
  readonly faTimes = faTimes;
  resultMessage:string = "";

  onEditUser(){
    alert("Ist geklickt")
  }


  onDeleteUser(id:number){
    this.userService.onDeleteUser(id).subscribe((deleted) => {
      if(deleted) {
        this.resultMessage = "Löschen war erfolgreich";
      }
      else{
        this.resultMessage = "Löschen war nicht erfolgreich";
      }
      this.popup?.openDialog();
    }
      /*
      Button für ok -> im HTML?
      this.dialog.openDialog;
       */
      );
  }

  ngOnInit(): void {
    const obs = this.userService.getAllUser();
    obs.subscribe(e => this.users = e);
  }

  onAddButton(){
    this.profileFormNewUser.setValue({
      newFirstname: '',
      newLastname: '',
      newUserEmail: '',
      newPassword: '',
      newUsername: '',
      newRole: ''
    });
    this.dialogNewUser?.openDialog();
  }

  onAddNewUser(){

  }

}
