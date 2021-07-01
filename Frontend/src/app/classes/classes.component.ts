import {Component, OnInit, ViewChild} from '@angular/core';
import {HttpClient} from "@angular/common/http";
//Models
import {Classes} from "../models/class.model";
import {Student} from "../models/student.model";
import {User} from "../models/user.model"
//Font Awesome Symbole
import {faTrash} from '@fortawesome/free-solid-svg-icons';
import {faPen} from '@fortawesome/free-solid-svg-icons';
import {faUserPlus} from '@fortawesome/free-solid-svg-icons';
import {faFolderPlus} from '@fortawesome/free-solid-svg-icons';
import {faAngleUp} from '@fortawesome/free-solid-svg-icons';
import {faAngleDown} from '@fortawesome/free-solid-svg-icons';
import{faUserMinus} from '@fortawesome/free-solid-svg-icons';
//Komponenten und Services
import {DialogComponent} from "../dialog/dialog.component";
import {UserService} from "../user.service";
import {ClassesService} from "../classes.service";
import {StudentService} from "../student.service";
import {combineLatest} from "rxjs";
import {FormControl, FormGroup, Validators} from "@angular/forms";


@Component({
  selector: 'app-classes',
  templateUrl: './classes.component.html',
  styleUrls: ['./classes.component.css']
})
export class ClassesComponent implements OnInit {
  @ViewChild('dialogNew') dialogNewClass?: DialogComponent;
  @ViewChild('dialog') dialog?: DialogComponent;
  @ViewChild('dialogDel') dialogDel?: DialogComponent;

  profileFormNewClass = new FormGroup({
    klassenName: new FormControl('',[Validators.required])
  });

  profileForm = new FormGroup({
    klassenID: new FormControl({value:'', disabled:true}),
    klassenName : new FormControl('',[Validators.required])
  });

  profileFormDel = new FormGroup({
    delklassenID: new FormControl({value:'', disabled:true}),
    delklassenName : new FormControl({value:'', disabled:true})
  });

  constructor(private classesService: ClassesService, private studenService: StudentService) { }
  classes: (Classes & {students: Student[]})[] = []; //classes hat Klassen + dazu alle dazugehörigen Schüler*innen
  cardsExpanded: boolean[] = [];
  readonly faTrash = faTrash;
  readonly faUserPlus = faUserPlus;
  readonly faFolderPlus = faFolderPlus;
  readonly faAngleUp = faAngleUp;
  readonly faPen = faPen;
  readonly faAngleDown = faAngleDown;
  readonly faUserMinus = faUserMinus;

  ngOnInit(): void {
    const obsclass = this.classesService.getAllClasses();
    const obsstudent = this.studenService.getAllStudents();
    combineLatest([obsclass, obsstudent]).subscribe(([classes, students]) => {
      if(!!classes && !!students){
        this.classes = classes.map((klasse) => {
          const filteredStudents = students.filter((student) => {
            return student.klasse === klasse.klassenID;
          });
          return {
            ...klasse,
            students: filteredStudents
          }
        });

        this.cardsExpanded = classes.map((klasse) => {
          return false;
        });
      }
    });
    console.log(this.classes);

  }

  onExpandingCard(index: number){
    this.cardsExpanded[index] = !this.cardsExpanded[index];
}
  onAddButton(){
    this.profileFormNewClass.setValue({
      klassenName: ''
    });
    this.dialogNewClass?.openDialog();
  }

  onAddNewClass(){
    let formData = this.profileFormNewClass.getRawValue();
    let newClass: Omit<Classes, 'klassenID'> = {
      klassenName: formData.klassenName
    };
    this.classesService.addNewClass(newClass).subscribe((createdClass: Classes) => {
      this.classes.push({
          ...createdClass,
          students:[]
        });
    });
    this.dialogNewClass?.closeDialog();
  }

  onEditButton(currentClass:Classes){
    this.profileForm.setValue({
      klassenID: currentClass.klassenID,
      klassenName : currentClass.klassenName
    });
    this.dialog?.openDialog();
  }

  onEditClass(){
    let formData = this.profileForm.getRawValue();
    let editClass: Classes = {
      klassenID: formData.klassenID,
      klassenName : formData.klassenName

    };
    this.classesService.onEditClass(editClass).subscribe(() => {
      const index: number = this.classes.findIndex((classes: Classes) => {return editClass.klassenID === classes.klassenID});
      this.classes[index].klassenID = editClass.klassenID;
      this.classes[index].klassenName = editClass.klassenName;
    });
    this.dialog?.closeDialog();
  }

  onDeleteButton(classDel: Classes){
    this.profileFormDel.setValue({
      delklassenID: classDel.klassenID,
      delklassenName : classDel.klassenName
    });
    this.dialogDel?.openDialog();
  }
  onDeleteClass(){
    /*
    index des eintrages suchen
    löschen
     */
    let delID: number = this.profileFormDel.getRawValue().delklassenID;
    this.classesService.onDeleteClass(delID).subscribe(() => {
      /*
      const index: number = this.classes.findIndex((classes: Classes) => {return editClass.klassenID === classes.klassenID});
      this.classes[index].klassenID = editClass.klassenID;
      this.classes[index].klassenName = editClass.klassenName;
    });
       */
      const index: number = this.classes.findIndex((classes: Classes) => {return delID === classes.klassenID});
      this.classes.splice(index, 1);
      }
    );
    this.dialogDel?.closeDialog();
  }
}
