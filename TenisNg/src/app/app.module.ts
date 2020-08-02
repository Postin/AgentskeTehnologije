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

@NgModule({
  declarations: [
    AppComponent,
    DefaultPageComponent,
    AgentListComponent,
    NavBarComponent
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
