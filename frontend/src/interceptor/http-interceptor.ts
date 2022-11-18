import {Observable} from 'rxjs';
import {Injectable} from '@angular/core';
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest, HttpResponse} from '@angular/common/http';
import {tap} from 'rxjs/operators';
import {SpinnerService} from "../service/spinner-service";
import {ErrorService} from "../service/error-service";

@Injectable()
export class CustomHttpInterceptor implements HttpInterceptor {

  constructor(private spinnerService: SpinnerService, private errorService: ErrorService) {
  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    this.spinnerService.show();

    return next.handle(req)
      .pipe(tap(({
        next: event => {
          if (event instanceof HttpResponse) {
            this.spinnerService.hide();
          }
        }, error: (errorResponse) => {
          this.errorService.handleError(errorResponse)
          this.spinnerService.hide();
        }
      })));
  }
}
