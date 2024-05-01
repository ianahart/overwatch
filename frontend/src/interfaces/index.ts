import { Role } from '../enums';

export interface ISignUpForm {
  form: {
    firstName: { name: string; value: string; error: string; type: string };
    lastName: { name: string; value: string; error: string; type: string };
    email: { name: string; value: string; error: string; type: string };
    password: { name: string; value: string; error: string; type: string };
    confirmPassword: { name: string; value: string; error: string; type: string };
    role: { name: string; value: Role; error: string; type: string };
  };
}
