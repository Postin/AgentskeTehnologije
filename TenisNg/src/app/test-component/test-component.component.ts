import { Component, OnInit } from '@angular/core';
import {HttpClient} from '@angular/common/http';

@Component({
  selector: 'app-test-component',
  templateUrl: './test-component.component.html',
  styleUrls: ['./test-component.component.css']
})
export class TestComponentComponent implements OnInit {

  constructor(private http:HttpClient) { }

  ngOnInit(): void {

  }

  sendTest():void{
    this.http.get("http://192.168.0.20:8080/TenisWAR/rest/node/test").subscribe(
      res=>{},
      err=>{alert("Something went wrong");}
    )
  }



}
