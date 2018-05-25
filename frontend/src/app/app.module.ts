import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import { ServiceWorkerModule } from '@angular/service-worker';
import {AppComponent} from './app.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {MaterialModule} from './material.module';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {HomeComponent} from './home/home.component';
import {AppRoutingModule} from './app-routing.module';
import {RegisterComponent} from './register/register.component';
import {HttpClient, HttpClientModule} from '@angular/common/http';
import {LoginComponent} from './login/login.component';
import {AuthService} from './service/auth.service';
import {AuthGuard} from './guard/auth.guard';
import {UserGuard} from './guard/user.guard';
import {ResearchfacilityGuard} from './guard/researchfacility.guard';
import {JwtModule} from '@auth0/angular-jwt';
import {TranslateLoader, TranslateModule} from '@ngx-translate/core';
import {TranslateHttpLoader} from '@ngx-translate/http-loader';
import {RegisterService} from './service/register.service';
import {HealthdataComponent} from './healthdata/healthdata.component';
import {HealthdataService} from './service/healthdata.service';
import {FlexLayoutModule} from '@angular/flex-layout';
import {QueriesComponent} from './healthdata/queries/queries.component';
import {MatChipsModule} from '@angular/material/chips';
import {MatTableModule, MatInputModule, MatCommonModule, MatCheckboxModule, MatProgressBarModule} from '@angular/material';
import {MedicalqueryComponent} from './medicalquery/medicalquery.component';
import {MedicalqueryService} from './service/medicalquery.service';
import {SharedHealthdataComponent} from './medicalquery/shared-healthdata/shared-healthdata.component';
import {environment} from '../environments/environment';
import { NotificationService } from './service/notification.service';


export function tokenGetter() {
  return localStorage.getItem('access_token');
}

export function HttpLoaderFactory(http: HttpClient) {
  return new TranslateHttpLoader(http);
}

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    RegisterComponent,
    LoginComponent,
    HealthdataComponent,
    QueriesComponent,
    MedicalqueryComponent,
    SharedHealthdataComponent
  ],
  imports: [
    BrowserModule,
    ServiceWorkerModule.register('/ngsw-worker.js', {
      enabled: true
    }),
    BrowserAnimationsModule,
    MaterialModule,
    FormsModule,
    FlexLayoutModule,
    ReactiveFormsModule,
    HttpClientModule,
    MatChipsModule,
    MatInputModule,
    MatCommonModule,
    MatCheckboxModule,
    MatProgressBarModule,
    MatTableModule,
    TranslateModule.forRoot({
      loader: {
        provide: TranslateLoader,
        useFactory: HttpLoaderFactory,
        deps: [HttpClient]
      }
    }),
    JwtModule.forRoot({
      config: {
        tokenGetter: tokenGetter,
        whitelistedDomains: [environment.domain],
        blacklistedRoutes: [environment.domain + '/auth', environment.domain + '/user/register']
      }
    }),
    AppRoutingModule
  ],
  providers: [
    AuthService,
    RegisterService,
    HealthdataService,
    MedicalqueryService,
    AuthGuard,
    UserGuard,
    ResearchfacilityGuard,
    NotificationService,
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
