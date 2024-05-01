import { configureStore } from '@reduxjs/toolkit';
import navbarReducer from './navbar/navbarSlice';
import signUpReducer from './signup/signUpSlice';

export const store = configureStore({
  reducer: {
    navbar: navbarReducer,
    signup: signUpReducer,
  },
});

export type TRootState = ReturnType<typeof store.getState>;
export type TAppDispatch = typeof store.dispatch;
