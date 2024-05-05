import { createSlice, Draft, PayloadAction } from '@reduxjs/toolkit';
import { IFormField, ISignInForm } from '../../interfaces';

interface ISignInFormState extends ISignInForm {}

const initialState: ISignInFormState = {
  email: { name: 'email', value: '', error: '', type: 'email' },
  password: { name: 'password', value: '', error: '', type: 'password' },
};

const signInSlice = createSlice({
  name: 'signIn',
  initialState,
  reducers: {
    updateSignInField: <T extends string>(
      state: Draft<ISignInFormState>,
      action: PayloadAction<{ name: string; value: T; attribute: keyof IFormField<T> }>
    ) => {
      const { name, value, attribute } = action.payload;
      state[name as keyof ISignInForm][attribute as keyof IFormField<T>] = value;
    },
    clearSignInForm: () => {
      return initialState;
    },
  },
});

export const { updateSignInField, clearSignInForm } = signInSlice.actions;

export const signInReducer = signInSlice.reducer;
