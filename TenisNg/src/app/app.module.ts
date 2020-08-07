import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { DefaultPageComponent } from './default-page/default-page.component';
import {HttpClient, HttpClientModule} from "@angular/common/http";
import { AgentListComponent } from './default-page/agent-list/agent-list.component';
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { FormsModule } from "@angular/forms";
import { NavBarComponent } from './default-page/nav-bar/nav-bar.component';
import { AgentClassesComponent } from './default-page/agent-classes/agent-classes.component';
import { PerformativesListComponent } from './default-page/performatives-list/performatives-list.component';
import { MessagePageComponent } from './default-page/message-page/message-page.component';

@NgModule({
  declarations: [
    AppComponent,
    DefaultPageComponent,
    AgentListComponent,
    NavBarComponent,
    AgentClassesComponent,
    PerformativesListComponent,
    MessagePageComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    NgbModule,
    HttpClientModule,
    BrowserModule,
    BrowserAnimationsModule,
    FormsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
