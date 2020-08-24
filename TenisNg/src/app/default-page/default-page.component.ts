import { Component, OnInit } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Router} from '@angular/router';

@Component({
  selector: 'app-default-page',
  templateUrl: './default-page.component.html',
  styleUrls: ['./default-page.component.css']
})
export class DefaultPageComponent implements OnInit {

  constructor(private http:HttpClient, public _route:Router) {

  }

  ngOnInit(): void {
  }

  address = "";

  sendTest():void{

    this.address = window.location.href.split(":")[1];
    this.address = this.address.substring(2);

    let url = "http://"+this.address+":8080/TenisWAR/rest/agents/test";

    this.http.get(url, {responseType:'text'}).subscribe(
      res=>{alert(res.toString());},
      err=>{alert(err.message);}
    )
  }
}
