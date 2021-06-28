import { Component, OnInit } from '@angular/core';
import {HttpClient} from "@angular/common/http";
//Font Awesome Symbole
import {faPen} from '@fortawesome/free-solid-svg-icons';
import {faTrash} from '@fortawesome/free-solid-svg-icons';
import {faTimes} from '@fortawesome/free-solid-svg-icons';
//Komponenten und Services
import {DialogComponent} from "../dialog/dialog.component";
import {UserService} from "../user.service";
import {BackendService} from "../backend.service";

@Component({
  selector: 'app-classes',
  templateUrl: './classes.component.html',
  styleUrls: ['./classes.component.css']
})
export class ClassesComponent implements OnInit {

  constructor() { }
  //classes: string[];
  readonly faPen = faPen;
  readonly faTrash = faTrash;
  readonly faTimes = faTimes;

  ngOnInit(): void {
   // const obs = this.backendService.getAllUser();
    //obs.subscribe(e => this.classes = e);
  }


}
