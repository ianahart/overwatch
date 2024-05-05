import { Role } from '../enums';

export interface ITokens {
  token: string;
  refreshToken: string;
}

export interface IUser {
  abbreviation: string;
  avatarUrl: string;
  email: string;
  firstName: string;
  lastName: string;
  fullName: string;
  id: number;
  loggedIn: boolean;
  profileId: number;
  role: string;
  settingId: number;
  slug: string;
}

export interface IFormField<T> {
  [key: string]: string | T;
  name: string;
  value: T;
  error: string;
  type: string;
}

export interface ISignUpForm {
  firstName: IFormField<string>;
  lastName: IFormField<string>;
  email: IFormField<string>;
  password: IFormField<string>;
  confirmPassword: IFormField<string>;
  role: IFormField<Role>;
}

export interface ISignInForm {
  email: IFormField<string>;
  password: IFormField<string>;
}

export interface ISignOut {
  refreshToken: string;
  token: string;
}

export interface ISignUpResponse {
  message: string;
}

export interface ISignOutResponse {
  messagE: string;
}

export interface ISignInResponse {
  token: string;
  refreshToken: string;
  user: IUser;
}

export interface ISyncUserResponse extends IUser {}
