import { Component, OnInit } from '@angular/core';
import {UserService} from "../user.service";
import { getGravatarHash, getGravatarUrl, getGravatarAvatar } from "gravatar-utils";

@Component({
  selector: 'app-navigation-profile',
  templateUrl: './navigation-profile.component.html',
  styleUrls: ['./navigation-profile.component.css']
})
export class NavigationProfileComponent implements OnInit {
  constructor(private userService: UserService) { } //klein geschriebene ist Member this.userService, UserService ist Typ
  role: string ="";
  name:string = "";
  profilepic : any;

  ngOnInit(): void {
    this.role = this.userService.getRole();
    this.name = this.userService.getName();
  }

}
