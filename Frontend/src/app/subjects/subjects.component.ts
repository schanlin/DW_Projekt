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
import {faPlus} from '@fortawesome/free-solid-svg-icons';
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
  readonly faPlus = faPlus;

  @ViewChild('dialogEditSubject') dialogEditSubject?: DialogComponent;
  @ViewChild('dialogDeleteSubject') dialogDeleteSubject? :DialogComponent;
  @ViewChild('dialogNewSubject') dialogNewSubject? :DialogComponent;
  @ViewChild('dialogArchivedSubject') dialogArchivedSubject? :DialogComponent;

  profileFormEditSubject = new FormGroup({
    subjectID: new FormControl({value:'', disabled:true}),
    subjectName: new FormControl('',[Validators.required])
  });

  profileFormDeleteSubject = new FormGroup({
    subjectID: new FormControl({value:'', disabled: true}),
    subjectName: new FormControl({value:'', disabled: true})
  })

  profileFormNewSubject = new FormGroup({
    subjectID: new FormControl({value:'', disabled: true}),
    subjectName: new FormControl('', [Validators.required])
  })

  profileFormArchivedSubject = new FormGroup({
    subjectID: new FormControl({value:'', disabled: true}),
    subjectName: new FormControl({value:'', disabled: true}),
    klasse: new FormControl({value:'', disabled: true}),
    teacher:new FormControl({value:'', disabled: true}),
    archived: new FormControl({value:'', disabled: true})
  })

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
      obsclasses
    ]);
    observable.subscribe(data =>{
      this.subjects = data[0];
      this.teacher = data[1];
      this.classes = data[2];

      console.log(this.dialogArchivedSubject, this.dialogEditSubject, this.dialogNewSubject);
      console.log(this.teacher);
      console.log(this.classes);

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
  }

  findIndexofSubject(searchedSubjectID: number){
    return this.subjects.findIndex((subject: Subject) => {
      if (searchedSubjectID === subject.subjectID){
        return true;
      }
      return false;
    });
  }

  onEditButton(currentSubject:Subject){
    this.profileFormEditSubject.setValue({
      subjectID: currentSubject.subjectID,
      subjectName : currentSubject.subjectName
    });
    console.log(this.dialogEditSubject);
    this.dialogEditSubject?.openDialog();
  }


  onEditSubject(){

  }

  onAddSubjectButton(){
    this.profileFormNewSubject.setValue({
      subjectID : '',
      subjectName: ''
    });
    this.dialogNewSubject?.openDialog();
  }

  onAddSubject(){ //TODO
    /*
    let formData = this.profileFormNewUser.getRawValue(); //was zurückkommt sieht so aus, wie der Usertyp, den ich zuweise, weil der aus der FormGroup kommt
    let newUser: Omit<User, 'userID'> = {
      username: formData.newUsername,
      firstname: formData.newFirstname,
      lastname: formData.newLastname,
      email: formData.newUserEmail,
      password: formData.newPassword,
      rolle: formData.newRole
    };
    this.userService.addNewUser(newUser).subscribe((createdUser: User) => {
      this.users.push(createdUser);
    });
    this.dialogNewUser?.closeDialog();
  }
     */
    let formData = this.profileFormNewSubject.getRawValue();
    let newSubject: Omit<Subject, 'subjectID' | 'teacher' | 'klasse'> = {
      subjectName: formData.subjectName,
      archived: false,
      teacherName: "",
      className: ""
    };
  this.subjectService.addNewSubject(newSubject).subscribe((createdSubject: Subject) => {
    this.subjects.push(createdSubject);
  });
    this.dialogNewSubject?.closeDialog();
  }

  onDeleteButton(deleteSubject:Subject){
    this.profileFormDeleteSubject.setValue({
      subjectID: deleteSubject.subjectID,
      subjectName: deleteSubject.subjectName
    });
    this.dialogDeleteSubject?.openDialog();
  }

  onDeleteSubject(){ //TODO -> 409 sinnvoll abfangen
    const delID: number = this.profileFormDeleteSubject.getRawValue().subjectID;
    this.subjectService.deleteSubject(delID).subscribe(() =>
      {
        const delIndex: number = this.findIndexofSubject(delID);
        this.subjects.slice(delIndex, 1);
      });
    this.dialogDeleteSubject?.closeDialog();
  }

  onArchiveButton(currentSubject:Subject){
    this.profileFormArchivedSubject.setValue({
      subjectID: currentSubject.subjectID,
      subjectName: currentSubject.subjectName,
      klasse: currentSubject.klasse,
      teacher: currentSubject.teacher,
      archived: true,
      });
    this.dialogArchivedSubject?.openDialog();
      /*Fenster öffnen-> wenn sie das archivieren werden Alle Fächer und Tests losgelöst
       ja/ nein
       currentSubject füllen, archived auf true setzen
    */
  }
  archiveSubject(){
    const archivedSubject : Omit<Subject, 'teacherName' | 'className'> = {
      subjectID: this.profileFormArchivedSubject.getRawValue().subjectID,
      subjectName: this.profileFormArchivedSubject.getRawValue().subjectName,
      klasse: this.profileFormArchivedSubject.getRawValue().klasse,
      teacher: this.profileFormArchivedSubject.getRawValue().teacher,
      archived: this.profileFormArchivedSubject.getRawValue().archived,
    };
    this.subjectService.archiveSubject(archivedSubject).subscribe();
    const index: number = this.findIndexofSubject(archivedSubject.subjectID);
    this.subjects[index].archived= true;

  }
}
