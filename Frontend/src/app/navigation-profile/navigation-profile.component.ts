import {Component, OnInit} from '@angular/core';
import {UserService} from "../user.service";
import {User} from "../models/user.model";
import JsGravatar from '@gravatar/js';

import { FormGroup, FormControl, Validators } from '@angular/forms';
import {faTimes} from '@fortawesome/free-solid-svg-icons';
import {faPen} from '@fortawesome/free-solid-svg-icons';
import {Observable} from "rxjs";

@Component({
  selector: 'app-navigation-profile',
  templateUrl: './navigation-profile.component.html',
  styleUrls: ['./navigation-profile.component.css']
})
export class NavigationProfileComponent implements OnInit {
  constructor(private userService: UserService) { } //klein geschriebene ist Member this.userService, UserService ist Typ
  role: string ="";
  firstname:string = "";
  lastname:string ="";
  profilepic : string = "";
  userEmail: string = "";
  faPen = faPen;
  faTimes = faTimes;
  id: number = 0;
  username: string = "";

  profileForm = new FormGroup({
    id: new FormControl({disabled:true}),
    firstname: new FormControl('', [Validators.required]),
    lastname: new FormControl('', Validators.required),
    userEmail: new FormControl('', [Validators.required, Validators.email]),
    password: new FormControl('', [Validators.required, Validators.minLength(6)]),
    username: new FormControl('', Validators.required),
  })

  ngOnInit(): void {
    const user: Observable<User> = this.userService.getCurrentUser();
    user.subscribe(e => {
      this.firstname = e.firstname;
      this.lastname = e.lastname;
      this.id = e.userID;
      this.userEmail = "";
      this.username = e.username;
      this.role = e.rolle;
    })
    this.profilepic = JsGravatar({email: this.userEmail, size: 100, protocol: 'https', defaultImage: 'identicon'});
  }


    /*this.profileForm.setValue({
      id: this.userService.getID(),
      firstname: this.userService.getFirstname(),
      lastname: this.userService.getLastname(),
      userEmail: this.userService.getEmailAdress(),
      password: '123456',
      username: this.userService.getUsername()
    });
    this.profileForm.controls['id'].disable();
    this.role = this.userService.getRole();
    this.firstname = this.userService.getFirstname();
    this.lastname = this.userService.getLastname();
    this.userEmail = this.userService.getEmailAdress();
    this.id = this.userService.getID();*/


  //}

}
