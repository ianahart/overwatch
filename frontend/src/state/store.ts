import { configureStore } from '@reduxjs/toolkit';
import { setupListeners } from '@reduxjs/toolkit/dist/query';
import { navbarReducer, openMobile, closeMobile } from './slices/navbarSlice';
import { signUpReducer, updateField, updateRole } from './slices/signupSlice';
import { authsApi } from './apis/authsApi';

export const store = configureStore({
  reducer: {
    navbar: navbarReducer,
    signup: signUpReducer,
    [authsApi.reducerPath]: authsApi.reducer,
  },
  middleware: (getDefaultMiddleware) => {
    return getDefaultMiddleware().concat(authsApi.middleware);
  },
});

setupListeners(store.dispatch);

export type TRootState = ReturnType<typeof store.getState>;
export type TAppDispatch = typeof store.dispatch;

export { updateField, updateRole, openMobile, closeMobile };
export { useSignUpMutation } from './apis/authsApi';
