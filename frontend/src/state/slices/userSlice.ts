import { createSlice, PayloadAction } from '@reduxjs/toolkit';
import { IUser, ITokens } from '../../interfaces';

interface IUserState {
  user: IUser;
  token: string;
  refreshToken: string;
}

const initialState: IUserState = {
  user: {
    abbreviation: '',
    avatarUrl: '',
    email: '',
    firstName: '',
    fullName: '',
    id: 0,
    lastName: '',
    loggedIn: false,
    profileId: 0,
    role: '',
    settingId: 0,
    slug: '',
  },
  token: '',
  refreshToken: '',
};

const userSlice = createSlice({
  name: 'user',
  initialState,
  reducers: {
    updateUser: (state, action: PayloadAction<IUser>) => {
      state.user = action.payload;
    },
    updateTokens: (state, action: PayloadAction<ITokens>) => {
      const { token, refreshToken } = action.payload;
      state.token = token;
      state.refreshToken = refreshToken;
    },
    updateUserAndTokens: (
      state,
      action: PayloadAction<{ user: IUser; tokens: { token: string; refreshToken: string } }>
    ) => {
      const { token, refreshToken } = action.payload.tokens;
      state.user = action.payload.user;
      state.token = token;
      state.refreshToken = refreshToken;
    },
    clearUser: () => {
      return initialState;
    },
  },
});

export const { updateUser, updateTokens, clearUser, updateUserAndTokens } = userSlice.actions;

export const userReducer = userSlice.reducer;
