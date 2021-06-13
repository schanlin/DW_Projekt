import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { UserComponent } from './user/user.component';
import { SubjectsComponent } from './subjects/subjects.component';
import { ClassesComponent } from './classes/classes.component';


export const routes: Routes = [
  { path: 'user',     component: UserComponent,     data: {role: ["admin"]} },
  { path: 'subjects', component: SubjectsComponent, data: {role: ["admin", "student", "teacher"]} },
  { path: 'classes',  component: ClassesComponent,  data: {role: ["admin", "student", "teacher"]} }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
