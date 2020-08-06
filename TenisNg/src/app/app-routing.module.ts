import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {AgentListComponent} from './default-page/agent-list/agent-list.component';
import {DefaultPageComponent} from './default-page/default-page.component';
import {AgentClassesComponent} from './default-page/agent-classes/agent-classes.component';
import {PerformativesListComponent} from './default-page/performatives-list/performatives-list.component';


const routes: Routes = [{
  path:'agents/types',
  component:AgentClassesComponent
},{
  path:'performatives/list',
  component:PerformativesListComponent
}];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
