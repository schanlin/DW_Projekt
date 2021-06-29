import { Component, OnInit } from '@angular/core';
import {routes} from "../app-routing.module";
import {UserService} from "../user.service";
import {Link} from "../models/link.model";
import {Observable} from "rxjs";
import {User} from "../models/user.model";


@Component({
  selector: 'app-navigation-link',
  templateUrl: './navigation-link.component.html',
  styleUrls: ['./navigation-link.component.css']
})
export class NavigationLinkComponent implements OnInit {

  constructor(private userService: UserService) {  }
  links: Link[] = [];
  role: string = "";
  ngOnInit(): void {
    const user: Observable<User> = this.userService.getCurrentUser();
    user.subscribe(e => {
      this.role = e.rolle;
      this.links = routes
        .filter(route => route.data!.roles.includes(this.role))
        .map(   route => { return {
          "display": route.data!.display,
          "path":    route.path!
        }});
    });
  }
}
