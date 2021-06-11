import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { NavigationComponent } from './navigation/navigation.component';
import { NavigationProfileComponent } from './navigation-profile/navigation-profile.component';
import { NavigationLinksComponent } from './navigation-links/navigation-links.component';

@NgModule({
  declarations: [
    AppComponent,
    NavigationComponent,
    NavigationProfileComponent,
    NavigationLinksComponent
  ],
  imports: [
    BrowserModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
