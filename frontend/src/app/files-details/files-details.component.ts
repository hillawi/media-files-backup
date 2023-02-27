import {Component, Input, OnInit} from '@angular/core';
import MediaDetails from "../home/home.component";

@Component({
  selector: 'app-files-details',
  templateUrl: './files-details.component.html',
  styleUrls: ['./files-details.component.css']
})
export class FilesDetailsComponent implements OnInit {

  @Input() title: string = '';
  @Input() files: MediaDetails[] = [];
  @Input() displayedColumns: string[] = [];

  constructor() {
  }

  ngOnInit(): void {
  }

}
