import {Component, OnInit, ViewChild} from '@angular/core';
import {combineLatest, forkJoin} from "rxjs";
import {Subject} from "../models/subject.model"
import {SubjectService} from "../subject.service";
import {Teacher} from "../models/teacher.model"
import {TeacherService} from "../teacher.service";
import {Classes} from "../models/class.model";
import {ClassesService} from "../classes.service";
import {faPen} from '@fortawesome/free-solid-svg-icons';
import {faTrash} from '@fortawesome/free-solid-svg-icons';
import {faTimes} from '@fortawesome/free-solid-svg-icons';
import {faFolderPlus} from '@fortawesome/free-solid-svg-icons';
import {faArchive} from '@fortawesome/free-solid-svg-icons';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {DialogComponent} from "../dialog/dialog.component";


@Component({
  selector: 'app-subjects',
  templateUrl: './subjects.component.html',
  styleUrls: ['./subjects.component.css']
})
export class SubjectsComponent implements OnInit {

  constructor(private subjectService: SubjectService, private teacherService: TeacherService, private classesService: ClassesService) { }
  subjects: Subject[] = [];
  teacher: Teacher[] = [];
  classes: Classes[] = [];
  readonly faPen = faPen;
  readonly faTrash = faTrash;
  readonly faTimes = faTimes;
  readonly faFolderPlus = faFolderPlus;
  readonly faArchive = faArchive;

  @ViewChild('dialogEditSubject') dialogEditSubject?: DialogComponent;

  profileFormEditSubject = new FormGroup({
    subjectID: new FormControl({value:'', disabled:true}),
    subjectName: new FormControl('',[Validators.required]),
  });

  ngOnInit(): void {
    const obssubjects = this.subjectService.getAllSubjects();
    //obssubjects.subscribe(e => this.subjects = e);

    const obsteacher = this.teacherService.getallTeacher();
    //obsteacher.subscribe(teacher => this.teacher = teacher);

    const obsclasses = this.classesService.getAllClasses();
    //obsclasses.subscribe(classes => this.classes = classes);

    const observable = forkJoin([
      obssubjects,
      obsteacher,
      obsclasses,
    ]);
    observable.subscribe(data =>{
      this.subjects = data[0];
      this.teacher = data[1];
      this.classes = data[2];

      console.log(this.subjects[0].subjectID);
      console.log(this.teacher[0].firstname);
      console.log(this.classes[0].klassenName);
      console.table(this.subjects);

      for(let i:number = 0; i<this.subjects.length; i++){
      for(let j:number = 0; j<this.classes.length; j++){
        if(this.subjects[i].klasse === this.classes[j].klassenID){
          this.subjects[i].className = this.classes[j].klassenName;
        }
      }
    }
    for(let i:number = 0; i<this.subjects.length; i++){
      for(let j:number = 0; j<this.teacher.length; j++){
        if(this.subjects[i].teacher === this.teacher[j].userID){
          this.subjects[i].teacherName = this.teacher[j].username;
        }
      }
    }
    });
    console.log("huhu");
    console.log(this.subjects[0].teacher);
  }

  onEditButton(currentSubject:Subject){
    this.profileFormEditSubject.setValue({
      subjectID: currentSubject.subjectID,
      subjectName : currentSubject.subjectName
    });
    this.dialogEditSubject?.openDialog();
  }
  onEditSubject(){

  }
}
