import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { UserComponent } from './user/user.component';
import { SubjectsComponent } from './subjects/subjects.component';
import { ClassesComponent } from './classes/classes.component';


export const routes: Routes = [
  { path: 'user',     component: UserComponent,     data: {roles: ["Administrator"],                       display: "Nutzende"} },
  { path: 'subjects', component: SubjectsComponent, data: {roles: ["Administrator", "Student", "Teacher"], display: "Fächer"} },
  { path: 'classes',  component: ClassesComponent,  data: {roles: ["Administrator", "Student", "Teacher"], display: "Klassen"} }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
