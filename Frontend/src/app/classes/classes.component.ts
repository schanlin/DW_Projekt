import {Component, OnInit, ViewChild} from '@angular/core';
import {HttpClient} from "@angular/common/http";
//Models
import {Classes} from "../models/class.model";
import {Student} from "../models/student.model";
import {User} from "../models/user.model"
import {Subject} from "../models/subject.model";
//Font Awesome Symbole
import {faTrash} from '@fortawesome/free-solid-svg-icons';
import {faPen} from '@fortawesome/free-solid-svg-icons';
import {faUserPlus} from '@fortawesome/free-solid-svg-icons';
import {faFolderPlus} from '@fortawesome/free-solid-svg-icons';
import {faAngleUp} from '@fortawesome/free-solid-svg-icons';
import {faAngleDown} from '@fortawesome/free-solid-svg-icons';
import{faUserMinus} from '@fortawesome/free-solid-svg-icons';
import {faPlus} from "@fortawesome/free-solid-svg-icons/faPlus";
//Komponenten und Services
import {DialogComponent} from "../dialog/dialog.component";
import {UserService} from "../user.service";
import {ClassesService} from "../classes.service";
import {StudentService} from "../student.service";
import {SubjectService} from "../subject.service";
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
  @ViewChild('dialogRemoveStudent') dialogRemoveStudent?:DialogComponent;
  @ViewChild('dialogRemoveSubject') dialogRemoveSubject?:DialogComponent;
  @ViewChild('dialogAddSubject') dialogAddSubject?:DialogComponent;

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

  profileFormRemoveStudent = new FormGroup({
    userID: new FormControl({value: '', disabled: true}),
    firstname: new FormControl({value:'', disabled:true}),
    lastname: new FormControl({value:'', disabled:true}),
    klasse: new FormControl({value:'', disabeld: true})
  })

  profileFormRemoveSubject = new FormGroup({
    subjectID: new FormControl({value: '', disabled: true}),
    subjectName: new FormControl({value:'', disabled:true}),
    klasse: new FormControl({value:'', disabled:true})
  })

  profileFormAddSubject = new FormGroup({
    subjectID : new FormControl('',[Validators.required])
  });

  constructor(private classesService: ClassesService, private studenService: StudentService, private subjectService: SubjectService) { }
  classes: (Classes & {students: Student[], subjects: Subject[] })[] = []; //classes hat Klassen + dazu alle dazugehörigen Schüler*innen
  studentsList: Student[] = [];
  subjectsList: Subject[] = [];
  cardsExpanded: boolean[] = [];
  readonly faTrash = faTrash;
  readonly faUserPlus = faUserPlus;
  readonly faFolderPlus = faFolderPlus;
  readonly faAngleUp = faAngleUp;
  readonly faPen = faPen;
  readonly faAngleDown = faAngleDown;
  readonly faUserMinus = faUserMinus;
  readonly faPlus = faPlus;
  currentClassID: number = 0;



  ngOnInit(): void {
    const obssubjects = this.subjectService.getAllSubjects();
    obssubjects.subscribe((subjects: Subject[]) => {
      this.subjectsList = subjects;
    })
    const obsclass = this.classesService.getAllClasses();
    const obsstudent = this.studenService.getAllStudents();
    obsstudent.subscribe((students: Student[]) => {
      this.studentsList = students;
    });
    combineLatest([obsclass, obsstudent, obssubjects]).subscribe(([classes, students, subjects]) => {
      if(!!classes && !!students && !! subjects){
        this.classes = classes.map((klasse) => {
          const filteredStudents = students.filter((student) => {
            return student.klasse === klasse.klassenID;
          });

          const filteredSubjects = subjects.filter((subject:Subject)=> {
            return subject.klasse === klasse.klassenID;
          });
          console.log(students, klasse, filteredStudents);
          return {
            ...klasse,
            students: filteredStudents,
            subjects: filteredSubjects
          }
        });

        this.cardsExpanded = classes.map((klasse) => {
          return false;
        });
      }
    });
    //console.log(this.classes);

    /* Eingabefeld bei der Auswahl des Lernenden, das username und email anzeigt
    this.profileFormAddStudent.controls["userID"].valueChanges.subscribe((id: string) => {
      if(id.length > 0) {
        const newStudent: Student = this.studentsList.find((student) => {
          return student.userID === Number.parseInt(id);
        }) as Student;
        console.log(newStudent, id);
        this.profileFormAddStudent.setValue({

          email: newStudent.email,
          username: newStudent.username
        });
      }
    })*/

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

  onAddNewClass() {
    let formData = this.profileFormNewClass.getRawValue();
    let newClass: Omit<Classes, 'klassenID'> = {
      klassenName: formData.klassenName
    };
    this.classesService.addNewClass(newClass).subscribe((createdClass: Classes) => {
      this.classes.push({
        ...createdClass,
        students: [],
        subjects: []
      });
    });
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
    return this.classes.findIndex((classes: Classes) => {
      if (searchedClassID === classes.klassenID){
        return true;
      }
      return false;
    });
  }

  findIndexofSubject(searchedsubjectID: number){
    return this.subjectsList.findIndex((subject: Subject) => {
      if (searchedsubjectID === subject.subjectID){
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
    let studentID = Number.parseInt(this.profileFormAddStudent.getRawValue().userID);
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
    console.log(this.studentsList, studentID, this.studentsList.find((students: Student) => {
      return studentID === students.userID
    }));
  this.dialogAddStudent?.closeDialog();
  }

  onAddSubjectButton(classID: number){ //TODO: PlusIcon html
    this.profileFormAddSubject.setValue({
      subjectID : ''
    });
    this.currentClassID = classID;
    this.dialogAddSubject?.openDialog();
  }

  onAddSubject(){
    let subjectID = Number.parseInt(this.profileFormAddSubject.getRawValue().subjectID);
    let classIndex = this.findIndexofClass(this.currentClassID);
    let subjectIndex = this.findIndexofSubject(subjectID);
    let editedSubject = this.subjectsList.find((subject: Subject)=>{
      return(subjectID === subject.subjectID);
    }) as Subject;
    editedSubject.klasse = this.currentClassID;
    this.subjectService.updateSubject(editedSubject).subscribe(() =>{
      this.classes[classIndex].subjects.push(editedSubject);
      this.subjectsList[subjectIndex].klasse = this.currentClassID;
      });
    this.dialogAddStudent?.closeDialog();
  }

  onRemoveStudentButton(current: Student){
    this.profileFormRemoveStudent.setValue({
      userID: current.userID,
      lastname: current.lastname,
      firstname: current.firstname,
      klasse: current.klasse
      });
    this.dialogRemoveStudent?.openDialog();
  }
  onRemoveStudent(){
    const detachedStudentID: number = this.profileFormRemoveStudent.getRawValue().userID;
    this.classesService.onAssignStudent(detachedStudentID, 0).subscribe();
    const currentClassID: number = this.profileFormRemoveStudent.getRawValue().klasse;
    const currentClassIndex: number = this.findIndexofClass(currentClassID);
    let currentStudentIndex:number = this.classes[currentClassIndex].students.findIndex((student: Student) =>{
      return (student.userID=== detachedStudentID);
    });
    this.classes[currentClassIndex].students.splice(currentStudentIndex, 1);
    currentStudentIndex= this.studentsList.findIndex((student: Student) =>{
      return(student.userID === detachedStudentID);
    });
    this.studentsList[currentStudentIndex].klasse = 0;
    this.dialogRemoveStudent?.closeDialog();
  }

  onRemoveSubjectButton(current: Subject){
    this.profileFormRemoveSubject.setValue({
      subjectID: current.subjectID,
      subjectName: current.subjectName,
      klasse: current.klasse
    });
    this.dialogRemoveSubject?.openDialog();
  }
  onRemoveSubject(){
    const detachedSubjectID: number = this.profileFormRemoveSubject.getRawValue().subjectID;
    //this.classesService.onAssignSubject(detachedSubjectID, 0).subscribe();
    const currentClassID: number = this.profileFormRemoveSubject.getRawValue().klasse;
    const currentClassIndex: number = this.findIndexofClass(currentClassID);
    let currentSubjectIndexClass:number = this.classes[currentClassIndex].subjects.findIndex((subject: Subject) =>{
      return (subject.subjectID=== detachedSubjectID);
    });
    let currentSubjectIndexSubjects= this.subjectsList.findIndex((subject: Subject) =>{
      return(subject.subjectID === detachedSubjectID);
    });
    const subject = {...this.subjectsList[currentSubjectIndexSubjects]};
    subject.klasse = 0;
    this.subjectService.updateSubject(subject).subscribe(() => {
      this.classes[currentSubjectIndexClass].students.splice(currentSubjectIndexClass, 1);
      this.subjectsList[currentSubjectIndexSubjects] = subject;
    });
    /*const subject: Subject = {
      subjectID: this.subjectsList[currentSubjectIndexSubjects].subjectID,
    }*/
    this.dialogRemoveSubject?.closeDialog();
  }

}
