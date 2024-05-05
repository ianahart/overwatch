import { Role } from '../enums';

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

export interface ISignUpResponse {
  message: string;
}
