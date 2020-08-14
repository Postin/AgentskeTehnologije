import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { AID } from 'src/app/_models/AID';

@Component({
    selector: 'app-agent-list',
    templateUrl: './agent-list.component.html',
    styleUrls: ['./agent-list.component.css']
  })
  
export class AgentListComponent implements OnInit {
  @ViewChild('agentType') agentType: ElementRef;
  @ViewChild('agentName') agentName: ElementRef;

  router: String;
  agents: AID[] = [];
  socket: any;
  host = "";
  address = "";

  constructor(_router:Router, private http:HttpClient) {
    this.router=_router.url;
  }

  public ngOnInit() { 
    let call = this;
    this.address = window.location.href.split(":")[1];
    this.address = this.address.substring(2);
    console.log(this.address);
    // this.address = "192.168.56.1"; // ovo zakomentarisati posle
    this.host = "ws://" + this.address + ":8080/TenisWAR/ws";
    try {
      this.socket = new WebSocket(this.host);

      this.socket.onopen = function () {
        console.log('onopen');
      }

      this.socket.onmessage = function (msg) {
        // Dodati sve funkcije koje treba da se refreshuju stalno
        console.log('onmessage: Received: ' + msg.data);
        call.getAllAgents();
      }

      this.socket.onclose = function () {
        call.socket = null;
      }

    } catch (exception) {
      console.log('Error' + exception);
    }
  }
  
  getAllAgents() {
    let url = "http://"+this.address+":8080/TenisWAR/rest/agents/running";
    console.log(url);
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
    let url = "http://"+this.address+":8080/TenisWAR/rest/agents/running";
    const options = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json'
      }),
      body: agent,
      responseType: 'text' as const,
    }
    this.http.delete(url, options).subscribe(
      (res:string) => {
        alert(res);
        this.getAllAgents();
      },
      err => {
        alert('Something went wrong');
        console.log(err.message);
      }
    )
  }

  runAgent(): void {
    let agentT: string = this.agentType.nativeElement.value;
    let agentN: string = this.agentName.nativeElement.value;
    const options = {
      responseType: 'text' as const,
    }
    let url = "http://"+this.address+":8080/TenisWAR/rest/agents/running/" + agentT + "/" + agentN;
    this.http.put(url, {agentT, agentN}, options).subscribe(
      (res:string) => {
        alert(res);
        this.agentType.nativeElement.value = "Master";
        this.agentName.nativeElement.value = ""; 
        this.getAllAgents();
      },
      err => {
        alert('Something went wrong');
        console.log(err.message);
      }
    )
  }
    
}