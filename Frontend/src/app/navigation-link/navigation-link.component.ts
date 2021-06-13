import { Component, OnInit } from '@angular/core';
import {routes} from "../app-routing.module";
import {UserService} from "../user.service";

@Component({
  selector: 'app-navigation-link',
  templateUrl: './navigation-link.component.html',
  styleUrls: ['./navigation-link.component.css']
})
export class NavigationLinkComponent implements OnInit {

  constructor(private userService: UserService) {  }
  links: string[] = []; //array


  ngOnInit(): void {
    const role: string = this.userService.getRole();


  }

}
