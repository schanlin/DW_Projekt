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
  @ViewChild('dialogAddStudent') dialogAddStudent?: DialogComponent;

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

  profileFormAddStudent = new FormGroup({
    userID : new FormControl('',[Validators.required]),
    username: new FormControl({value:'', disabled:true}),
    firstname: new FormControl({value:'', disabled:true}),
    lastname: new FormControl({value:'', disabled:true}),
    rolle: new FormControl({value:'', disabled:true}),
    email: new FormControl({value:'', disabled:true}),
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
    let delID: number = this.profileFormDel.getRawValue().delklassenID;
    this.classesService.onDeleteClass(delID).subscribe(() => {
      const index:number = this.findIndexofClass(delID);
      this.classes.splice(index, 1) ;
      }
    );
    this.dialogDel?.closeDialog();
  }

  findIndexofClass(searchedClassID: number){
    //return this.classes.findIndex((classes: Classes) => {return searchedClassID === classes.klassenID});
    return this.classes.findIndex((classes: Classes) => {
      if (searchedClassID === classes.klassenID){
        return true;
      }
      return false;
    });
  }

  onAddStudentButton(klassenID: number){
    this.profileFormAddStudent.setValue({
      userID : '',
      username: '',
      firstname: '',
      lastname: '',
      rolle: '',
      email: '',
    });
    this.currentClassID = klassenID;
    this.dialogAddStudent?.openDialog();
  }
  onAddStudent(){
    let studentID =this.profileFormAddStudent.getRawValue().userID;
    this.classesService.onAssignStudent(studentID, this.currentClassID).subscribe(); //API erwarter ein subscribe, weil Oservable zurückkommt

    for(let i = 0; i < this.classes.length; i++) {
      let studentIndex: number = this.classes[i].students.findIndex((students: Student) => {return studentID === students.userID});
      if(studentIndex !== -1) {
        this.classes[i].students.splice(studentIndex, 1);
        break;
      }
    }
    const classIndex = this.findIndexofClass(this.currentClassID);
    this.classes[classIndex].students.push(this.studentsList.find((students: Student) => {
      return studentID === students.userID
    }) as Student);
  this.dialogAddStudent?.closeDialog();
  }
}
