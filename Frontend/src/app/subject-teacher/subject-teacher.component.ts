import {Component, OnInit, ViewChild} from '@angular/core';
import {TestService} from '../test.service';
import {TeacherService} from "../teacher.service";
import {StudentService} from "../student.service";
import {UserService} from "../user.service";
import {Subject} from "../models/subject.model";
import {Student} from "../models/student.model";
import {TestResult} from "../models/test-result.model";
import {Test} from "../models/test.model";
import {SubjectService} from "../subject.service";
import {User} from "../models/user.model";
import {forkJoin} from "rxjs";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {DialogComponent} from "../dialog/dialog.component";
import {faAngleUp} from '@fortawesome/free-solid-svg-icons';
import {faAngleDown} from '@fortawesome/free-solid-svg-icons';
import {faPlus} from '@fortawesome/free-solid-svg-icons';
import {faMinus} from '@fortawesome/free-solid-svg-icons';

type StudentResult = Student & TestResult;
type TestStudentResult = Test & { students: StudentResult[] };
type StudentAverage = {sum: number, count: number};
type StudentAverageMap = {[studentID: number]: StudentAverage};
type SubjectTestStudentResult = Subject & {tests:TestStudentResult[], averages: StudentAverageMap};

@Component({
  selector: 'app-subject-teacher',
  templateUrl: './subject-teacher.component.html',
  styleUrls: ['./subject-teacher.component.css']
})
export class SubjectTeacherComponent implements OnInit {
  @ViewChild("dialogNewTest") dialogNewTest?: DialogComponent;
  @ViewChild("dialogNewResult") dialogNewResult?: DialogComponent;
  @ViewChild("dialogDeleteTest") dialogDeleteTest?: DialogComponent;

  profileFormNewTest = new FormGroup({
    testName: new FormControl('', [Validators.required]),
    testDate: new FormControl('', [Validators.required]),
    subject: new FormControl({value:'', disabled:true}),
    subjectID: new FormControl({value:'', disabled: true})
  });

  profileFormNewResult = new FormGroup({
    testID: new FormControl({value:'', disabled: true}),
    studentID: new FormControl('', [Validators.required]),
    mark: new FormControl('', [Validators.required])
  });

  profileFormDeleteTest = new FormGroup(({
    testID: new FormControl({value:'', disabled: true})
  }));

  subjects:SubjectTestStudentResult[] = [];
  students: Student[] = [];
  isExpanded: {
    subject: boolean;
    tests: boolean[];
  }[] = [];

  constructor(
    private userService: UserService,
    private teacherService: TeacherService,
    private studentService: StudentService,
    private testService: TestService) { }
    readonly faAngleUp= faAngleUp;
    readonly faAngleDown = faAngleDown;
    readonly faMinus = faMinus;
    readonly faPlus = faPlus;

  ngOnInit(): void {
    const currentUserObs = this.userService.getCurrentUser();
    const studentsObs = this.studentService.getAllStudents();

    forkJoin([currentUserObs,studentsObs])
    .subscribe((data) => {
      const currentUser: User = data[0];
      this.students = data[1];
      this.teacherService.getAllSubjectsForTeacher(currentUser.userID).subscribe((subjects: Subject[]) => {
        for (let subjectIndex = 0; subjectIndex < subjects.length; subjectIndex++) {
          this.subjects[subjectIndex] = {
            ...subjects[subjectIndex],
            tests: [],
            averages: {}
          };
          this.isExpanded[subjectIndex] = {
            subject: false,
            tests: []
          };
          const subjectID = subjects[subjectIndex].subjectID;
          this.teacherService.getAllTestsForSubject(subjectID).subscribe((tests: Test[]) => {
            for (let testIndex = 0; testIndex < tests.length; testIndex++) {
              this.isExpanded[subjectIndex].tests[testIndex] = false;
              this.subjects[subjectIndex].tests[testIndex] = {
                ...tests[testIndex],
                students: []
              };
              this.teacherService.getAllResultsForTest(tests[testIndex].testID).subscribe((results: TestResult[]) => {
                for (let resultIndex = 0; resultIndex < results.length; resultIndex++) {
                  const studentID = results[resultIndex].studentID;
                  const currentStudent = this.students.find((student: Student) => {
                    return student.userID === studentID;
                  }) as Student;
                  this.subjects[subjectIndex].tests[testIndex].students[resultIndex] = {
                    ...currentStudent,
                    ...results[resultIndex]
                  };

                  if(!this.subjects[subjectIndex].averages[studentID]){
                    this.subjects[subjectIndex].averages[studentID] = {
                      sum: 0,
                      count: 0
                    }
                  }
                  this.subjects[subjectIndex].averages[studentID].sum += results[resultIndex].mark;
                  this.subjects[subjectIndex].averages[studentID].count++;
                }
              });
            }
          });
        }
      });
    });
  }

  newTestButton(subject: Subject){
    this.profileFormNewTest.setValue({
      testName: '',
      testDate: '',
      subject: subject.subjectName,
      subjectID: subject.subjectID
    });
    this.dialogNewTest?.openDialog();
  }
  onAddTest(){
    let formValues: any = this.profileFormNewTest.getRawValue();
    const isoDate = new Date(formValues.testDate);
    let newTest: Test = {
      testID : 0,
      testName : formValues.testName,
      testDatum: isoDate.toISOString(),
      subject: formValues.subjectID
    }
    this.testService.addTest(newTest).subscribe(() => {
      const subjectIndex = this.subjects.findIndex((subject: SubjectTestStudentResult) => {
        return subject.subjectID === newTest.subject;
      });
      this.subjects[subjectIndex].tests.push({
        ...newTest,
        students: []
      });
    });
  }

  addResultToTest(test: Test) {
    this.profileFormNewResult.setValue({
      testID: test.testID,
      studentID: 0,
      mark: null
    });
    this.dialogNewResult?.openDialog();
  }

  onAddResultToTest() {
    const formValues = this.profileFormNewResult.getRawValue();
    const testResult: TestResult = {
      testID: formValues.testID,
      studentID: Number.parseInt(formValues.studentID),
      mark: formValues.mark
    };
    this.teacherService.addTestResult(testResult).subscribe(() => {
      for (let subjectIndex = 0; subjectIndex < this.subjects.length; subjectIndex++) {
        for (let testIndex = 0; testIndex < this.subjects[subjectIndex].tests.length; testIndex++) {
          if(this.subjects[subjectIndex].tests[testIndex].testID === testResult.testID){
            const currentStudent: Student = this.students.find((student: Student) => {
              return student.userID === testResult.studentID;
            }) as Student;
            this.subjects[subjectIndex].tests[testIndex].students.push({
              ...testResult,
              ...currentStudent
            });
            if (!this.subjects[subjectIndex].averages[testResult.studentID]) {
              this.subjects[subjectIndex].averages[testResult.studentID] = {
                sum: 0,
                count: 0
              };
              this.subjects[subjectIndex].averages[testResult.studentID].sum += testResult.mark;
              this.subjects[subjectIndex].averages[testResult.studentID].count++;
            }
            break;
          }
        }
      }
    });

    this.dialogNewResult?.closeDialog();
  }

  changeExpandSubject(index: number){
    this.isExpanded[index].subject = !this.isExpanded[index].subject;
  }

  changeExpandTest(subjectIndex: number, testIndex: number){
    this.isExpanded[subjectIndex].tests[testIndex] = !this.isExpanded[subjectIndex].tests[testIndex];
  }

  calculateAverage(studentAverage: StudentAverage) {
    return Math.round(studentAverage.sum / studentAverage.count * 100) / 100;
  }

  onDeleteTest() {
    this.testService.deleteTest(this.profileFormDeleteTest.getRawValue().testID).subscribe(() => {
      for (let subjectIndex = 0; subjectIndex < this.subjects.length; subjectIndex++) {
        const testIndex = this.subjects[subjectIndex].tests.findIndex((test) => {
          return test.testID === this.profileFormDeleteTest.getRawValue().testID;
        });
        if(testIndex !== -1){
          this.subjects[subjectIndex].tests.splice(testIndex, 1);
          break;
        }
      }
    });

    this.dialogDeleteTest?.closeDialog();
  }

  removeTest(test: Test) {
    this.profileFormDeleteTest.setValue({
      testID: test.testID
    });
    this.dialogDeleteTest?.openDialog();
  }
}
