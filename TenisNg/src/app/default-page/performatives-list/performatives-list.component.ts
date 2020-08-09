import { Component, OnInit } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import { PerformativeDTO } from 'src/app/_models/PerformativeDTO';

@Component({
  selector: 'app-performatives-list',
  templateUrl: './performatives-list.component.html',
  styleUrls: ['./performatives-list.component.css']
})
export class PerformativesListComponent implements OnInit {

  performatives: PerformativeDTO[] = [];
  constructor(private http:HttpClient) { }

  ngOnInit(): void {
   this.getPerformatives();
  }

  getPerformatives():void{
    let url = "http://localhost:8080/TenisWAR/rest/agents/messages";
    this.http.get(url).subscribe(
      (res:PerformativeDTO[])=>{this.performatives=res;},
      err=>{alert("Something went wrong"); console.log(err.message);}
    );
  }

}
