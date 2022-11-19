import {Injectable} from '@angular/core';
import {HttpErrorResponse} from "@angular/common/http";
import {NotificationService} from "../service/notification.service";

@Injectable({
  providedIn: 'root'
})
export class ErrorService {

  constructor(private notificationService: NotificationService) {
  }

  public handleError(errorResponse: HttpErrorResponse) {
    let errorMsg: string;
    if (errorResponse.error instanceof ErrorEvent) {
      errorMsg = `An error occurred: ${errorResponse.error.message}`;
    } else {
      switch (errorResponse.status) {
        case 400:
          errorMsg = `${errorResponse.status}: Bad request`;
          break;
        case 404:
          errorMsg = `${errorResponse.status}: Not found`;
          break;
        case 500:
          errorMsg = `${errorResponse.status}: Internal server error`;
          break;
        case 503:
          errorMsg = `${errorResponse.status}: The request service is not available`;
          break;
        default:
          errorMsg = 'Something went wrong';
      }
    }
    this.notificationService.notify(errorMsg, 'Error');
  }
}
