export class SharingPermission {
  id: string;
  information: string;
  queryId: string;

  constructor(id: string, information: string, queryId: string) {
    this.id = id;
    this.information = information;
    this.queryId = queryId;
  }
}
