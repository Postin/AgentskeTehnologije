import { Component, OnInit } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {AID} from '../../_models/AID';
import {PerformativeDTO} from '../../_models/PerformativeDTO';
import {ACLMessage} from '../../_models/ACLMessage';
import {Performative} from '../../_models/Performative';

@Component({
  selector: 'app-message-page',
  templateUrl: './message-page.component.html',
  styleUrls: ['./message-page.component.css']
})
export class MessagePageComponent implements OnInit {

  selectedSender:string;
  selectedReceivers:string[] = [];
  selectedPerformative:string;

  agents:AID[] = [];
  performatives:PerformativeDTO[] = [];
  alcMessage:ACLMessage = {
    performative: null,
    sender: null,
    receivers:[],
    replyTo:null,
    content:'',
    contentObj:null,
    userArgs:null,
    language:'',
    encoding:'',
    ontology:'',
    protocol:'',
    conversationId:'',
    replyWith:'',
    inReplyTo:'',
    replyBy:0

  };

  aid:AID ={
    name:'',
    host:null,
    type:null
  };

  address = "";

  constructor(private http:HttpClient) { }

  ngOnInit(): void {
    this.address = window.location.href.split(":")[1];
    this.address = this.address.substring(2);
    let url = "http://"+ this.address +":8080/TenisWAR/rest/agents/running";
    let urlp = "http://"+ this.address +":8080/TenisWAR/rest/agents/messages";
    this.http.get(url).subscribe(
      (res:AID[])=>{this.agents=res;},
      err=>{alert("Something went wrong"); console.log(err.message);}
    );

    this.http.get(urlp).subscribe(
      (res:PerformativeDTO[])=>{this.performatives=res;},
      err=>{alert("Something went wrong"); console.log(err.message);}
    );

  }

  sendMessage():void{
    console.log(this.alcMessage);

    let sender:AID = {name:this.selectedSender, type:null, host:null};
    this.alcMessage.sender = sender;

    let perf:PerformativeDTO = {performative:Performative[this.selectedPerformative]};
    this.alcMessage.performative = perf;

    let receivers:AID[] = [];
    for(let i = 0; i < this.selectedReceivers.length; i++){
      let r:AID = {name:this.selectedReceivers[i], type:null, host:null};
      this.alcMessage.receivers.push(r);
    }

    this.address = window.location.href.split(":")[1];
    this.address = this.address.substring(2);
    let url = "http://" +this.address+":8080/TenisWAR/rest/agents/messages";

    this.http.post(url, this.alcMessage, {responseType:'text'}).subscribe(
      res=>{alert("Message sent");},
      err=>{console.log(err.message); alert("Something went wrong");}
    )

  }
}
