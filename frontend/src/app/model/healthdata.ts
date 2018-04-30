export class HealthData {
  id: string;
  title: string;
  description: string;
  image: string;
  tags: Array<string>;
  userId: string;

  constructor(id: string, title: string, description: string, image: string, tags: Array<string>, userId: string) {
    this.id = id;
    this.title = title;
    this.description = description;
    this.image = image;
    this.tags = tags;
    this.userId = userId;
  }
}
