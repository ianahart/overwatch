import { configureStore } from '@reduxjs/toolkit';
import { setupListeners } from '@reduxjs/toolkit/dist/query';
import { navbarReducer, openMobile, closeMobile } from './slices/navbarSlice';
import { signUpReducer, updateSignUpField, updateRole, clearSignUpForm } from './slices/signupSlice';
import { updateSignInField, clearSignInForm, signInReducer } from './slices/signinSlice';
import { authsApi } from './apis/authsApi';
import { userReducer, updateUser, updateTokens, clearUser, updateUserAndTokens } from './slices/userSlice';
import { usersApi } from './apis/usersApi';

export const store = configureStore({
  reducer: {
    navbar: navbarReducer,
    signup: signUpReducer,
    signin: signInReducer,
    user: userReducer,
    [authsApi.reducerPath]: authsApi.reducer,
    [usersApi.reducerPath]: usersApi.reducer,
  },
  middleware: (getDefaultMiddleware) => {
    return getDefaultMiddleware().concat(authsApi.middleware).concat(usersApi.middleware);
  },
});

setupListeners(store.dispatch);

export type TRootState = ReturnType<typeof store.getState>;
export type TAppDispatch = typeof store.dispatch;

export {
  updateSignUpField,
  updateRole,
  openMobile,
  closeMobile,
  updateSignInField,
  updateUser,
  updateTokens,
  clearUser,
  clearSignInForm,
  clearSignUpForm,
  updateUserAndTokens,
};
export { useSignUpMutation, useSignInMutation, useSignOutMutation } from './apis/authsApi';
export { useSyncUserQuery } from './apis/usersApi';