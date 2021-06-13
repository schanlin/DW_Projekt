import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { NavigationComponent } from './navigation/navigation.component';
import { NavigationProfileComponent } from './navigation-profile/navigation-profile.component';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NavigationLinkComponent } from './navigation-link/navigation-link.component';
import { AppRoutingModule } from './app-routing.module';
import { UserComponent } from './user/user.component';
import { ClassesComponent } from './classes/classes.component';
import { SubjectsComponent } from './subjects/subjects.component';


@NgModule({
  declarations: [
    AppComponent,
    NavigationComponent,
    NavigationProfileComponent,
    NavigationLinkComponent,
    UserComponent,
    ClassesComponent,
    SubjectsComponent
  ],
  imports: [
    BrowserModule,
    FontAwesomeModule,
    AppRoutingModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
