export class HealthData {
  title: string;
  description: string;
  image: string;
  tags: Array<string>;

  constructor(title, description, image, tags) {
    this.title = title;
    this.description = description;
    this.image = image;
    this.tags = tags;
  }
}
