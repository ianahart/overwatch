import { Draft, PayloadAction, createSlice } from '@reduxjs/toolkit';
import { IFormField, IProfileSetupForm } from '../../interfaces';
import { clearUser } from '../store';

interface IProfileSetupFormState extends IProfileSetupForm {}

const initialState: IProfileSetupFormState = {
  avatar: { name: 'avatar', value: null, error: '', type: 'file' },
  tagLine: { name: 'tagLine', value: '', error: '', type: 'text' },
  bio: { name: 'bio', value: '', error: '', type: 'text' },
};

const profileSetupFormSlice = createSlice({
  name: 'profileSetup',
  initialState,
  reducers: {
    updateProfileSetupFormField: <T extends string>(
      state: Draft<IProfileSetupFormState>,
      action: PayloadAction<{ name: string; value: T; attribute: keyof IFormField<T> }>
    ) => {
      const { name, value, attribute } = action.payload;
      state[name as keyof IProfileSetupForm][attribute as keyof IFormField<T>] = value;
    },

    updateAvatar: (state, action: PayloadAction<File | null | string>) => {
      state.avatar.value = action.payload;
    },
    clearProfileSetupForm: () => {
      return initialState;
    },
  },
  extraReducers: (builder) => {
    builder.addCase(clearUser, () => {
      return initialState;
    });
  },
});

export const { clearProfileSetupForm, updateAvatar, updateProfileSetupFormField } = profileSetupFormSlice.actions;

export const profileSetupFormReducer = profileSetupFormSlice.reducer;
