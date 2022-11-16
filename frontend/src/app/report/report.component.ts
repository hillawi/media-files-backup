import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-report',
  templateUrl: './report.component.html',
  styleUrls: ['./report.component.css']
})
export class ReportComponent implements OnInit {

  displayedColumns: string[] = ['name', 'size'];

  constructor() { }

  ngOnInit(): void {
  }

}
