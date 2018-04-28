export class MedicalQuery {
  id: string;
  researchFacility: string;
  name: string;
  description: string;
  financialOffering: number;
  minAge: number;
  maxAge: number;
  gender: string;
  tags: string[];

  constructor(
    id,
    researchFacility,
    name,
    description,
    financialOffering,
    minAge,
    maxAge,
    gender,
    tags
  ) {
    this.id = id;
    this.researchFacility = researchFacility;
    this.name = name;
    this.description = description;
    this.financialOffering = financialOffering;
    this.minAge = minAge;
    this.maxAge = maxAge;
    this.gender = gender;
    this.tags = tags;
  }
}
