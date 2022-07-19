import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {HttpClient} from "@angular/common/http";
import {SpinnerService} from "../service/spinner-service";

interface Phone {
  id: string;
  name: string;
}

interface MediaType {
  id: string;
  name: string;
}

class ResponseMessage {
  output: string = '';
  error: string = '';
}

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  readonly ROOT_URL = 'http://localhost:8387';
  readonly phones: Phone[] = [
    {name: 'OnePlus 6T', id: '6t'},
    {name: 'OnePlus 7T', id: '7t'}
  ];
  readonly mediaTypes: MediaType[] = [
    {name: 'Pictures', id: 'IMG'},
    {name: 'Videos', id: 'VID'}
  ];

  successfulRequest: boolean | null = null;
  responseMessage = {};

  phoneControl = new FormControl<Phone | null>(null, Validators.required);
  mediaTypeControl = new FormControl<MediaType | null>(null, Validators.required);
  backupForm = new FormGroup({
    phone: this.phoneControl,
    mediaType: this.mediaTypeControl
  });

  constructor(private http: HttpClient, private spinnerService: SpinnerService) {
  }

  ngOnInit() {
  }

  onSubmit() {
    const body = {mediaTypeId: this.backupForm.value.mediaType?.id, phoneId: this.backupForm.value.phone?.id};
    const options = {};

    this.http.post<ResponseMessage>(this.ROOT_URL + '/launchBackup', body, options)
      .subscribe({
        next: (data) => {
          console.log(data.output);
          this.successfulRequest = true;
          this.responseMessage = data.output;
        }, error: (error) => {
          console.log(error);
          this.successfulRequest = false;
          this.responseMessage = error.error.text;
        }
      });
  }

  showSpinner() {
    return this.spinnerService.visibility;
  }
}
