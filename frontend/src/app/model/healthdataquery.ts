import {HealthDataShare} from './healthdatashare';

export class HealthDataQuery {
  id: string;
  name: string;
  description: string;
  institute: string;
  price: number;
  healthData: Array<HealthDataShare>;

  constructor(id: string, name: string, description: string, institute: string, price: number, healthData: Array<HealthDataShare>) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.institute = institute;
    this.price = price;
    this.healthData = healthData;
  }
}
