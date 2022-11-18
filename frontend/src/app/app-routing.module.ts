import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {HomeComponent} from "./home/home.component";
import {ErrorComponent} from "./error/error.component";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {ToastrModule} from "ngx-toastr";

const routes: Routes = [
  {path: '', redirectTo: '/home', pathMatch: 'full'},
  {path: 'home', component: HomeComponent},
  {path: '**', component: ErrorComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes), BrowserAnimationsModule, ToastrModule.forRoot({
    timeOut: 3000,
    progressBar: true,
    progressAnimation: 'increasing',
    preventDuplicates: true
  })],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
