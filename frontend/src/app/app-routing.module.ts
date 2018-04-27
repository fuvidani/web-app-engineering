import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {HomeComponent} from './home/home.component';
import {RegisterComponent} from './register/register.component';
import {LoginComponent} from './login/login.component';
import {HealthdataComponent} from './healthdata/healthdata.component';
import {UserGuard} from './guard/user.guard';
import {QueriesComponent} from './healthdata/queries/queries.component';
import {ResearchfacilityGuard} from './guard/researchfacility.guard';
import {MedicalqueryComponent} from './medicalquery/medicalquery.component';
import {SharedHealthdataComponent} from './medicalquery/shared-healthdata/shared-healthdata.component';

const routes: Routes = [
  {path: '', component: HomeComponent},
  {path: 'register', component: RegisterComponent},
  {path: 'login', component: LoginComponent},
  {path: 'healthdata', component: HealthdataComponent, canActivate: [UserGuard]},
  {path: 'userqueries', component: QueriesComponent, canActivate: [UserGuard]},
  {path: 'medicalquery', component: MedicalqueryComponent, canActivate: [ResearchfacilityGuard]},
  {path: 'medicalquery/:qid/shared', component: SharedHealthdataComponent, canActivate: [ResearchfacilityGuard]},
  {path: '**', component: HomeComponent}
];

@NgModule({
  imports: [
    RouterModule.forRoot(routes)
  ],
  exports: [RouterModule],
  declarations: []
})

export class AppRoutingModule {
}
