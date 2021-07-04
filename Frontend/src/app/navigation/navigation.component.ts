import { Component, OnInit } from '@angular/core';
import {faPowerOff} from "@fortawesome/free-solid-svg-icons";

@Component({
  selector: 'app-navigation',
  templateUrl: './navigation.component.html',
  styleUrls: ['./navigation.component.css']
})
export class NavigationComponent implements OnInit {
  readonly faPowerOff = faPowerOff;
  constructor() { }

  ngOnInit(): void {
  }

  logout(){
    window.location.assign('/api/logout');
  }
}
