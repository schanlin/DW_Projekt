import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { UserComponent } from './user/user.component';
import { SubjectsComponent } from './subjects/subjects.component';
import { ClassesComponent } from './classes/classes.component';
import { SubjectTeacherComponent} from "./subject-teacher/subject-teacher.component";


export const routes: Routes = [
  { path: 'user',             component: UserComponent,           data: {roles: ["Admin"],                         display: "Nutzende"} },
  { path: 'subjects',         component: SubjectsComponent,       data: {roles: ["Admin", "Lernende"],             display: "Fächer"} },
  { path: 'classes',          component: ClassesComponent,        data: {roles: ["Admin", "Lernende", "Lehrende"], display: "Klassen"} },
  { path: 'subjectsTeacher',  component: SubjectTeacherComponent, data: {roles: ['Lehrende'],                      display: "Fächer"}}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
