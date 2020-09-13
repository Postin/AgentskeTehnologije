import { Component, OnInit } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {ACLMessage} from '../../_models/ACLMessage';
import {AID} from '../../_models/AID';

@Component({
  selector: 'app-all-messages',
  templateUrl: './all-messages.component.html',
  styleUrls: ['./all-messages.component.css']
})
export class AllMessagesComponent implements OnInit {

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

  socket: any;
  host = "";
  address = "";

  aclMessages:ACLMessage[] = [];

  constructor(private http:HttpClient) { }

  ngOnInit(): void {
    this.getMessages();

    let call = this;
    this.address = window.location.href.split(":")[1];
    this.address = this.address.substring(2);
    console.log(this.address);
    this.address = "192.168.56.1"; // ovo zakomentarisati posle
    this.host = "ws://" + this.address + ":8080/TenisWAR/ws";
    try {
      this.socket = new WebSocket(this.host);

      this.socket.onopen = function () {
        console.log('onopen');
      }

      this.socket.onmessage = function (msg) {
        // Dodati sve funkcije koje treba da se refreshuju stalno
        console.log('onmessage: Received: ' + msg.data);
        call.getMessages();
      }

      this.socket.onclose = function () {
        call.socket = null;
      }

    } catch (exception) {
      console.log('Error' + exception);
    }
  }

  getMessages():void{
<<<<<<< HEAD
    let url = "http://192.168.56.1:8080/TenisWAR/rest/agents/inbox";
=======
    let url = "http://192.168.0.20:8080/TenisWAR/rest/agents/inbox";
>>>>>>> parent of cb212fa3... Fixed addresses

    this.http.get(url).subscribe(
      (res:ACLMessage[]) => {this.aclMessages = res;},
      err=>{alert("Something went wrong"); console.log(err.message);}
    );

  }

  receiversToString(message:ACLMessage):string{
    let retVal:string = '';

    for(let r of message.receivers){
      retVal += r.name + ', ';
    }

    retVal = retVal.substr(0, retVal.length-2);
    return retVal;
  }
}
