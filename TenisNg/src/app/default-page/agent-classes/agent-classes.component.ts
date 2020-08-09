import { Component, OnInit } from '@angular/core';
import {AgentType} from '../../_models/AgentType';
import {HttpClient} from '@angular/common/http';

@Component({
  selector: 'app-agent-classes',
  templateUrl: './agent-classes.component.html',
  styleUrls: ['./agent-classes.component.css']
})
export class AgentClassesComponent implements OnInit {

  agentClasses:AgentType[] = [];

  constructor(private http:HttpClient) { }

  ngOnInit(): void {
    this.getAgentClasses();
  }

  getAgentClasses():void{
    let url = "http://192.168.56.1:8080/TenisWAR/rest/agents/classes";
    this.http.get(url).subscribe(
      (res:AgentType[])=>{this.agentClasses=res;},
      err=>{alert("Something went wrong"); console.log(err.meessage);}
    );
  }

}
