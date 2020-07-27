import { Component, OnInit } from '@angular/core';
import {HttpClient} from "@angular/common/http";

@Component({
  selector: 'app-default-page',
  templateUrl: './default-page.component.html',
  styleUrls: ['./default-page.component.css']
})
export class DefaultPageComponent implements OnInit {

  constructor(private http:HttpClient) { }

  ngOnInit(): void {
  }

  sendTest():void{
    let url = "http://localhost:8080/TenisWAR/rest/agents/test";

    this.http.get(url, {responseType:'text'}).subscribe(
      res=>{alert(res.toString());},
      err=>{alert(err.message);}
    )
  }
}
