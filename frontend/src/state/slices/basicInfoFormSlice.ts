import { Draft, PayloadAction, createSlice } from '@reduxjs/toolkit';
import { IBasicInfoForm, IBasicInfoResponse, IFormField } from '../../interfaces';
import { clearUser } from '../actions/globalActions';

interface IBasicInfoFormState extends IBasicInfoForm {}

const initialState: IBasicInfoFormState = {
  fullName: { name: 'fullName', value: '', error: '', type: 'text' },
  userName: { name: 'userName', value: '', error: '', type: 'text' },
  email: { name: 'email', value: '', error: '', type: 'email' },
  contactNumber: { name: 'contactNumber', value: '', error: '', type: 'text' },
};

const basicInfoFormSlice = createSlice({
  name: 'basicInfo',
  initialState,
  reducers: {
    updateBasicInfo: (state, action: PayloadAction<IBasicInfoResponse>) => {
      const { payload } = action;
      for (let prop in payload) {
        state[prop as keyof IBasicInfoFormState].value = payload[prop as keyof IBasicInfoResponse] ?? '';
      }
    },

    updateBasicInfoFormField: <T extends string>(
      state: Draft<IBasicInfoFormState>,
      action: PayloadAction<{ name: string; value: T; attribute: keyof IFormField<T> }>
    ) => {
      const { name, value, attribute } = action.payload;
      state[name as keyof IBasicInfoForm][attribute as keyof IFormField<T>] = value;
    },

    clearBasicInfoForm: () => {
      return initialState;
    },
  },
  extraReducers: (builder) => {
    builder.addCase(clearUser, () => {
      return initialState;
    });
  },
});

export const { updateBasicInfo, clearBasicInfoForm, updateBasicInfoFormField } = basicInfoFormSlice.actions;

export const basicInfoFormReducer = basicInfoFormSlice.reducer;
