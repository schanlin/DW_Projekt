import {Component, OnInit} from '@angular/core';
import {UserService} from "../user.service";
import JsGravatar from '@gravatar/js';
import {faPen} from '@fortawesome/free-solid-svg-icons';


@Component({
  selector: 'app-navigation-profile',
  templateUrl: './navigation-profile.component.html',
  styleUrls: ['./navigation-profile.component.css']
})
export class NavigationProfileComponent implements OnInit {
  constructor(private userService: UserService) { } //klein geschriebene ist Member this.userService, UserService ist Typ
  role: string ="";
  name:string = "";
  profilepic : string = "";
  userEmail: string = "";
  faPen = faPen;

  ngOnInit(): void {
    this.role = this.userService.getRole();
    this.name = this.userService.getName();
    this.userEmail = this.userService.getEmailAdress();
    this.profilepic = JsGravatar({email: this.userEmail, size: 100, protocol: 'https', defaultImage: 'identicon'});
  }

}
