import {HealthData} from './healthdata';

export class AnonymizedSharedData {
  id: string;
  userId: string;
  birthday: string;
  gender: string;
  medicalInformation: Array<HealthData>;

  constructor(id: string, userId: string, birthday: string, gender: string, medicalInformation: Array<HealthData>) {
    this.id = id;
    this.userId = userId;
    this.birthday = birthday;
    this.gender = gender;
    this.medicalInformation = medicalInformation;
  }
}
