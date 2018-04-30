export class MedicalQuery {
  id: string;
  researchFacilityId: string;
  name: string;
  description: string;
  financialOffering: number;
  minAge: number;
  maxAge: number;
  gender: string;
  tags: string[];

  constructor(
    id,
    researchFacilityId,
    name,
    description,
    financialOffering,
    minAge,
    maxAge,
    gender,
    tags
  ) {
    this.id = id;
    this.researchFacilityId = researchFacilityId;
    this.name = name;
    this.description = description;
    this.financialOffering = financialOffering;
    this.minAge = minAge;
    this.maxAge = maxAge;
    this.gender = gender;
    this.tags = tags;
  }
}
