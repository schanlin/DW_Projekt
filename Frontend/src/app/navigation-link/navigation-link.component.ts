import { Component, OnInit } from '@angular/core';
import {routes} from "../app-routing.module";
import {UserService} from "../user.service";

interface Link {
  display: string
  path: string
}

@Component({
  selector: 'app-navigation-link',
  templateUrl: './navigation-link.component.html',
  styleUrls: ['./navigation-link.component.css']
})
export class NavigationLinkComponent implements OnInit {

  constructor(private userService: UserService) {  }
  links: Link[] = []; //array

  ngOnInit(): void {
    const role: string = this.userService.getRole();
    this.links = routes
      .filter(route => route.data!.roles.includes(role))
      .map(   route => { return {
        "display": route.data!.display,
        "path":    route.path!
       }});

      //.map(   route => <Link>{
      //  "display": route.data!.display,
      //  "path":    route.data!.path
      // });
  }
}
