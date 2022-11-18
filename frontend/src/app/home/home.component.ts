import {Component, OnInit} from '@angular/core';
import {environment} from "../../environments/environment";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {HttpClient} from "@angular/common/http";
import {SpinnerService} from "../../service/spinner-service";
import {ToastrService} from "ngx-toastr";

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

export default class MediaDetails {
  name: string;
  size: number;

  constructor(name: string, size: number) {
    this.name = name;
    this.size = size;
  }
}

class ResponseMessage {
  processedFiles: MediaDetails[] = [];
  erroredFiles: MediaDetails[] = [];
}

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  readonly apiBaseUrl = environment.apiURL;
  readonly mediaTypes: MediaType[] = [
    {name: 'Pictures', id: 'IMG'},
    {name: 'Videos', id: 'VID'}
  ];

  sourceDevices: Device[] = [];
  targetDevices: Device[] = [];

  successfulRequest: boolean | null = null;
  processedFiles: MediaDetails[] = [];
  erroredFiles: MediaDetails[] = [];

  reportReady: boolean = false;

  displayedColumns: string[] = ['name', 'size'];

  sourceDeviceControl = new FormControl<Device | null>(null, Validators.required);
  targetDeviceControl = new FormControl<Device | null>(null, Validators.required);
  mediaTypeControl = new FormControl<MediaType | null>(null, Validators.required);
  backupForm = new FormGroup({
    sourceDevice: this.sourceDeviceControl,
    targetDevice: this.targetDeviceControl,
    mediaType: this.mediaTypeControl
  });

  constructor(private http: HttpClient, private spinnerService: SpinnerService, private toastr: ToastrService) {
  }

  ngOnInit() {
    this.initDevices();
  }

  initDevices(): void {
    this.http.get<Device[]>(this.apiBaseUrl + '/devices').subscribe(res => {
      this.sourceDevices = res.filter(device => device.type === 'source');
      this.targetDevices = res.filter(device => device.type === 'target');
    });
  }

  onSubmit(): void {
    //this.toastr.success('Hello world!', 'Toastr fun!');
/*    const body = {
      mediaType: this.backupForm.value.mediaType?.id,
      sourceDeviceId: this.backupForm.value.sourceDevice?.id,
      targetDeviceId: this.backupForm.value.targetDevice?.id
    };
    const options = {};

    this.http.post<ResponseMessage>(this.apiBaseUrl + '/launchBackup', body, options)
      .subscribe({
        next: (data) => {
          this.successfulRequest = true;
          this.processedFiles = data.processedFiles;
          this.erroredFiles = data.erroredFiles;
          this.reportReady = true;
        }, error: (error) => {
          console.log("error:", error)
          this.successfulRequest = false;
          this.reportReady = false;
        }
      });*/
  }

  showSpinner() {
    return this.spinnerService.visibility;
  }

  onNewBackupClick() {
    this.reportReady = false;
  }
}
