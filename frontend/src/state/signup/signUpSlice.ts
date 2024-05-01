import { createSlice, PayloadAction } from '@reduxjs/toolkit';
import { ISignUpForm } from '../../interfaces';
import { Role } from '../../enums';

interface ISignUpFormState extends ISignUpForm {}

const initialState: ISignUpFormState = {
  form: {
    firstName: { name: 'firstName', value: '', error: '', type: 'text' },
    lastName: { name: 'lastName', value: '', error: '', type: 'text' },
    email: { name: 'email', value: '', error: '', type: 'email' },
    password: { name: 'password', value: '', error: '', type: 'password' },
    confirmPassword: {
      name: 'confirmPassword',
      value: '',
      error: '',
      type: 'password',
    },
    role: { name: 'role', value: Role.UNASSIGNED, error: '', type: 'text' },
  },
};

const signUpSlice = createSlice({
  name: 'signUp',
  initialState,
  reducers: {
    updateRole: (state, action: PayloadAction<Role>) => {
      state.form.role.value = action.payload;
    },
  },
});

export const { updateRole } = signUpSlice.actions;

export default signUpSlice.reducer;
