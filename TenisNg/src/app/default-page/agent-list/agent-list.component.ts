import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { AID } from 'src/app/_models/AID';

@Component({
    selector: 'app-agent-list',
    templateUrl: './agent-list.component.html',
    styleUrls: ['./agent-list.component.css']
  })
  
export class AgentListComponent implements OnInit {
    
  router: String;
  agents: AID[] = [];

  constructor(_router:Router, private http:HttpClient) {
    this.router=_router.url;
  }

  public ngOnInit() { 
    this.getAllAgents();
  }
  
  getAllAgents() {
    let url = "http://localhost:8080/TenisWAR/rest/agents/running";
    this.http.get(url).subscribe(
      (res:AID[])=>{
        this.agents = res;
      },
      err=>{
        alert('Something went wrong');
        console.log(err.message);
      }
    )
  }

  stopAgent(agent: AID):void {
    let url = "http://localhost:8080/TenisWAR/rest/agents/running";
    this.http.delete(url, { responseType: 'text' }).subscribe(
      res => {
        this.getAllAgents();
      },
      err => {
        alert('Something went wrong');
        console.log(err.message);
      }
    )
  }
    
}