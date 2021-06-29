import { Component, OnInit } from '@angular/core';
import {HttpClient} from "@angular/common/http";
//Models
import {Classes} from "../models/class.model";
//Font Awesome Symbole
import {faTrash} from '@fortawesome/free-solid-svg-icons';
import {faPen} from '@fortawesome/free-solid-svg-icons';
import {faUserPlus} from '@fortawesome/free-solid-svg-icons';
import {faFolderPlus} from '@fortawesome/free-solid-svg-icons';
import {faAngleUp} from '@fortawesome/free-solid-svg-icons';


//Komponenten und Services
import {DialogComponent} from "../dialog/dialog.component";
import {ClassesService} from "../classes.service";

@Component({
  selector: 'app-classes',
  templateUrl: './classes.component.html',
  styleUrls: ['./classes.component.css']
})
export class ClassesComponent implements OnInit {

  constructor(private classesService: ClassesService) { }
  classes: Classes[] = [];
  readonly faTrash = faTrash;
  readonly faUserPlus = faUserPlus;
  readonly faFolderPlus = faFolderPlus;
  readonly faAngleUp = faAngleUp;
  readonly faPen = faPen;

  ngOnInit(): void {
    const obs = this.classesService.getAllClasses();
    obs.subscribe(e => this.classes = e);
  }
}
