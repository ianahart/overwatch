import { configureStore } from '@reduxjs/toolkit';
import { setupListeners } from '@reduxjs/toolkit/dist/query';
import { navbarReducer, openMobile, closeMobile } from './slices/navbarSlice';
import { signUpReducer, updateSignUpField, updateRole } from './slices/signupSlice';
import { updateSignInField } from './slices/signinSlice';
import { authsApi } from './apis/authsApi';
import { signInReducer } from './slices/signinSlice';

export const store = configureStore({
  reducer: {
    navbar: navbarReducer,
    signup: signUpReducer,
    signin: signInReducer,
    [authsApi.reducerPath]: authsApi.reducer,
  },
  middleware: (getDefaultMiddleware) => {
    return getDefaultMiddleware().concat(authsApi.middleware);
  },
});

setupListeners(store.dispatch);

export type TRootState = ReturnType<typeof store.getState>;
export type TAppDispatch = typeof store.dispatch;

export { updateSignUpField, updateRole, openMobile, closeMobile, updateSignInField };
export { useSignUpMutation } from './apis/authsApi';
