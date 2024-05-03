import { configureStore } from '@reduxjs/toolkit';
import { navbarReducer, openMobile, closeMobile } from './slices/navbarSlice';
import { signUpReducer, updateField, updateRole } from './slices/signupSlice';

export const store = configureStore({
  reducer: {
    navbar: navbarReducer,
    signup: signUpReducer,
  },
});

export type TRootState = ReturnType<typeof store.getState>;
export type TAppDispatch = typeof store.dispatch;
export { updateField, updateRole, openMobile, closeMobile };
