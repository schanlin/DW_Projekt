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
  teachers: Teacher[] = [];
  classes: Classes[] = [];
  readonly faPen = faPen;
  readonly faTrash = faTrash;
  readonly faArchive = faArchive;
  readonly faPlus = faPlus;


  @ViewChild('dialogEditSubject') dialogEditSubject?: DialogComponent;
  @ViewChild('dialogDeleteSubject') dialogDeleteSubject? :DialogComponent;
  @ViewChild('dialogNewSubject') dialogNewSubject? :DialogComponent;
  @ViewChild('dialogArchivedSubject') dialogArchivedSubject? :DialogComponent;

  profileFormEditSubject = new FormGroup({
    subjectID: new FormControl({value:'', disabled:true}),
    subjectName: new FormControl('',[Validators.required]),
    teacher: new FormControl(''),
    klasse: new FormControl(''),
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
      this.teachers = data[1];
      this.classes = data[2];

      console.log(this.dialogArchivedSubject, this.dialogEditSubject, this.dialogNewSubject);
      console.log(this.teachers);
      console.log(this.classes);

    for(let i:number = 0; i<this.subjects.length; i++){
      for(let j:number = 0; j<this.classes.length; j++){
        if(this.subjects[i].klasse === this.classes[j].klassenID){
          this.subjects[i].className = this.classes[j].klassenName;
        }
      }
    }
    for(let i:number = 0; i<this.subjects.length; i++){
      for(let j:number = 0; j<this.teachers.length; j++){
        if(this.subjects[i].teacher === this.teachers[j].userID){
          this.subjects[i].teacherName = this.teachers[j].username;
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

  findTeacherByID(teacherID: number) {
    return this.teachers.find((searchedTeacher: Teacher) => {
        return (searchedTeacher.userID === teacherID);
      });
  }

  findClassByID(classID: number) {
    return this.classes.find((searchedClass: Classes) => {
      return( searchedClass.klassenID === classID);
    });
  }

  findSubjectbyID(subjectID: number){
    return this.subjects.find((searchedSubject: Subject) => {
      return (searchedSubject.subjectID === subjectID);
    });
  }

  onEditButton(currentSubject:Subject){
    this.profileFormEditSubject.setValue({
      subjectID: currentSubject.subjectID,
      subjectName : currentSubject.subjectName,
      teacher: currentSubject.teacher,
      klasse: currentSubject
    });
    console.log(this.dialogEditSubject);
    this.dialogEditSubject?.openDialog();
  }


  onEditSubject(){
    const formValues: Subject = this.profileFormEditSubject.getRawValue();
    console.log(formValues)
    formValues.teacher = Number.parseInt(formValues.teacher as any);
    formValues.klasse = Number.parseInt(formValues.klasse as any);
    let teacher: Teacher = this.findTeacherByID(formValues.teacher) as Teacher;
    let klasse: Classes = this.findClassByID(formValues.klasse) as Classes;
    let currentSubject: Subject = {...this.findSubjectbyID(formValues.subjectID)} as Subject;

    currentSubject.subjectName = formValues.subjectName;
    currentSubject.klasse = formValues.klasse;
    currentSubject.teacher = formValues.teacher;
    currentSubject.teacherName = teacher.username;
    currentSubject.className = klasse.klassenName;
    this.subjectService.updateSubject(currentSubject).subscribe(() => {
      const subjectIndex: number = this.findIndexofSubject(currentSubject.subjectID);
      this.subjects[subjectIndex] = currentSubject;
      });
    this.dialogEditSubject?.closeDialog();
  }

  onAddSubjectButton(){
    this.profileFormNewSubject.setValue({
      subjectID : '',
      subjectName: ''
    });
    this.dialogNewSubject?.openDialog();
  }

  onAddSubject(){
    let formData = this.profileFormNewSubject.getRawValue();
    let newSubject: Omit<Subject, 'subjectID' | 'teacherName' | 'className'> = {
      subjectName: formData.subjectName,
      archived: false,
      teacher: 0,
      klasse: 0
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
    if(currentSubject.archived){
      return;
    }
    this.profileFormArchivedSubject.setValue({
      subjectID: currentSubject.subjectID,
      subjectName: currentSubject.subjectName,
      klasse: currentSubject.klasse,
      teacher: currentSubject.teacher,
      archived: true,
      });
    this.dialogArchivedSubject?.openDialog();
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
