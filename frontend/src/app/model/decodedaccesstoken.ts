export class DecodedAccessToken {
  sub: string;
  email: string;
  roles: string[];
  exp: number;
  iat: number;

  constructor() { }
}
