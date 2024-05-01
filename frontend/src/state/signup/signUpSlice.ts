import { createSlice, Draft, PayloadAction } from '@reduxjs/toolkit';
import { IFormField, ISignUpForm } from '../../interfaces';
import { Role } from '../../enums';

interface ISignUpFormState extends ISignUpForm {}

const initialState: ISignUpFormState = {
  firstName: { name: 'firstName', value: '', error: '', type: 'text' },
  lastName: { name: 'lastName', value: '', error: '', type: 'text' },
  email: { name: 'email', value: '', error: '', type: 'email' },
  password: { name: 'password', value: '', error: '', type: 'password' },
  confirmPassword: { name: 'confirmPassword', value: '', error: '', type: 'password' },
  role: { name: 'role', value: Role.UNASSIGNED, error: '', type: 'text' },
};

const signUpSlice = createSlice({
  name: 'signUp',
  initialState,
  reducers: {
    updateRole: (state, action: PayloadAction<Role>) => {
      state.role.value = action.payload;
    },
    updateField: <T extends string>(
      state: Draft<ISignUpFormState>,
      action: PayloadAction<{ name: string; value: T; attribute: keyof IFormField<T> }>
    ) => {
      const { name, value, attribute } = action.payload;
      state[name as keyof ISignUpForm][attribute as keyof IFormField<T>] = value;
    },
  },
});

export const { updateRole, updateField } = signUpSlice.actions;

export default signUpSlice.reducer;
