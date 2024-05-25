import { PayloadAction, createSlice } from '@reduxjs/toolkit';
import { IProfileSetupForm } from '../../interfaces';
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

export const { clearProfileSetupForm, updateAvatar } = profileSetupFormSlice.actions;

export const profileSetupFormReducer = profileSetupFormSlice.reducer;
