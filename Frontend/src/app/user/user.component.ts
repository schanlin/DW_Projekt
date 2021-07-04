import { Component, OnInit, ViewChild } from '@angular/core';
import {DialogComponent} from "../dialog/dialog.component";
import {FormControl, FormGroup, Validators} from "@angular/forms";
//Service
import {UserService} from "../user.service";
//Models
import {User} from "../models/user.model";
//Font_Awesome Symbole
import {faPen} from '@fortawesome/free-solid-svg-icons';
import {faTrash} from '@fortawesome/free-solid-svg-icons';
import {faTimes} from '@fortawesome/free-solid-svg-icons';
import {faPlus} from "@fortawesome/free-solid-svg-icons/faPlus";

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css']
})
export class UserComponent implements OnInit {

  @ViewChild('dialogVar') dialogVar?: DialogComponent;
  @ViewChild('dialogDel') popup?: DialogComponent;
  @ViewChild('dialogNew') dialogNewUser?: DialogComponent;

  profileForm = new FormGroup({
    id: new FormControl({value:'', disabled: true}),
    firstname: new FormControl('', [Validators.required]),
    lastname: new FormControl('', [Validators.required]),
    userEmail: new FormControl('', [Validators.required, Validators.email]),
    password: new FormControl('', [Validators.required, Validators.minLength(6)]),
    username: new FormControl('',[Validators.required,Validators.minLength(6)]),
    role: new FormControl({value: '', disabled: true})
  });

  profileFormNewUser = new FormGroup({
    newFirstname: new FormControl ('Vorname' ,[Validators.required]), //das soll ein leeres Form werden, dass einen User ausspuckt
    newLastname: new FormControl('' ,[Validators.required]),//den ich ans Backend leite
    newUserEmail: new FormControl('',[Validators.required, Validators.email]),
    newPassword: new FormControl('',[Validators.required, Validators.minLength(6)]),
    newUsername: new FormControl('Username',[Validators.required]),
    newRole: new FormControl('', [Validators.required])
  });

  constructor(private userService: UserService) { }
  users: User[] = [];
  readonly faPen = faPen;
  readonly faTrash = faTrash;
  readonly faTimes = faTimes;
  readonly faPlus = faPlus;
  resultMessage:string = "";

  onEditButton(user: User){
    this.profileForm.setValue({
      id: user.userID,
      firstname: user.firstname,
      lastname: user.lastname,
      userEmail: user.email,
      password: user.password,
      username: user.username,
      role: user.rolle
    });
    this.dialogVar?.openDialog();
  }
  onEditUser(){
    let formData = this.profileForm.getRawValue();
    let editUser: User = {
      username: formData.username,
      firstname: formData.firstname,
      lastname: formData.lastname,
      email: formData.userEmail,
      password: formData.password,
      rolle: formData.role,
      userID: formData.id
    };
    this.userService.onEditUser(editUser).subscribe(() => {
      const index: number = this.users.findIndex((user: User) => {return editUser.userID === user.userID});
      this.users[index] = editUser;
      //console.log(this.users);
    });
    this.dialogVar?.closeDialog();
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
    let formData = this.profileFormNewUser.getRawValue(); //was zurückkommt sieht so aus, wie der Usertyp, den ich zuweise, weil der aus der FormGroup kommt
    let newUser: Omit<User, 'userID'> = {
      username: formData.newUsername,
      firstname: formData.newFirstname,
      lastname: formData.newLastname,
      email: formData.newUserEmail,
      password: formData.newPassword,
      rolle: formData.newRole
    };
    this.userService.addNewUser(newUser).subscribe((createdUser: User) => {
      this.users.push(createdUser);
    });
    this.dialogNewUser?.closeDialog();
  }

}
