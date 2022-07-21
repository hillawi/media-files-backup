import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {HttpClient} from "@angular/common/http";
import {SpinnerService} from "../service/spinner-service";

interface Device {
  id: string;
  uuid: string;
  name: string;
  owner: string;
  type: string;
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
  readonly mediaTypes: MediaType[] = [
    {name: 'Pictures', id: 'IMG'},
    {name: 'Videos', id: 'VID'}
  ];

  sourceDevices: Device[] = [];
  targetDevices: Device[] = [];

  successfulRequest: boolean | null = null;
  responseMessage = {};

  sourceDeviceControl = new FormControl<Device | null>(null, Validators.required);
  targetDeviceControl = new FormControl<Device | null>(null, Validators.required);
  mediaTypeControl = new FormControl<MediaType | null>(null, Validators.required);
  backupForm = new FormGroup({
    sourceDevice: this.sourceDeviceControl,
    targetDevice: this.targetDeviceControl,
    mediaType: this.mediaTypeControl
  });

  constructor(private http: HttpClient, private spinnerService: SpinnerService) {
  }

  ngOnInit() {
    this.initDevices();
  }

  initDevices(): void {
    this.http.get<Device[]>(this.ROOT_URL + '/devices').subscribe(res => {
      this.sourceDevices = res.filter(device => device.type === 'source');
      this.targetDevices = res.filter(device => device.type === 'target');
    });
  }

  onSubmit(): void {
    const body = {
      mediaType: this.backupForm.value.mediaType?.id,
      sourceDeviceId: this.backupForm.value.sourceDevice?.id,
      targetDeviceId: this.backupForm.value.targetDevice?.id
    };
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
