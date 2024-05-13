import { PayloadAction, createSlice } from '@reduxjs/toolkit';
import { ISetting } from '../../interfaces';

interface ISettingState {
  setting: ISetting;
}

const initialState: ISettingState = {
  setting: {
    id: 0,
    userId: 0,
    mfaEnabled: false,
    createdAt: '',
  },
};

const settingSlice = createSlice({
  name: 'setting',
  initialState,
  reducers: {
    updateSetting: (state, action: PayloadAction<ISetting>) => {
      state.setting = action.payload;
    },
    clearSetting: () => {
      return initialState;
    },
  },
});

export const { updateSetting, clearSetting } = settingSlice.actions;

export const settingReducer = settingSlice.reducer;
