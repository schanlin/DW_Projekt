import { Component, OnInit } from '@angular/core';
import {Subject} from "../models/subject.model"
import {SubjectService} from "../subject.service";
import {faPen} from '@fortawesome/free-solid-svg-icons';
import {faTrash} from '@fortawesome/free-solid-svg-icons';
import {faTimes} from '@fortawesome/free-solid-svg-icons';
import {faFolderPlus} from '@fortawesome/free-solid-svg-icons';
import {faArchive} from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-subjects',
  templateUrl: './subjects.component.html',
  styleUrls: ['./subjects.component.css']
})
export class SubjectsComponent implements OnInit {

  constructor(private subjectService: SubjectService) { }
  subjects: Subject[] = [];
  readonly faPen = faPen;
  readonly faTrash = faTrash;
  readonly faTimes = faTimes;
  readonly faFolderPlus = faFolderPlus;
  readonly faArchive = faArchive;

  ngOnInit(): void {
    const obs = this.subjectService.getAllSubjects();
    obs.subscribe(e => this.subjects = e);
  }


}
