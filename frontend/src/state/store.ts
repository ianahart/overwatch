import { configureStore } from '@reduxjs/toolkit';
import navbarReducer from './navbar/navbarSlice';

export const store = configureStore({
  reducer: {
    navbar: navbarReducer,
  },
});

export type TRootState = ReturnType<typeof store.getState>;
export type TAppDispatch = typeof store.dispatch;
