import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {AgentListComponent} from './default-page/agent-list/agent-list.component';
import {DefaultPageComponent} from './default-page/default-page.component';
import {AgentClassesComponent} from './default-page/agent-classes/agent-classes.component';
import {PerformativesListComponent} from './default-page/performatives-list/performatives-list.component';
import {MessagePageComponent} from './default-page/message-page/message-page.component';


const routes: Routes = [{
  path:'agents/types',
  component:AgentClassesComponent
},{
  path:'performatives/list',
  component:PerformativesListComponent
},
  {
    path:'messages',
    component:MessagePageComponent
  }];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
