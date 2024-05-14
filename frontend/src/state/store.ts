import { configureStore } from '@reduxjs/toolkit';
import { setupListeners } from '@reduxjs/toolkit/dist/query';
import { navbarReducer, openMobile, closeMobile } from './slices/navbarSlice';
import { signUpReducer, updateSignUpField, updateRole, clearSignUpForm } from './slices/signupSlice';
import { updateSignInField, clearSignInForm, signInReducer } from './slices/signinSlice';
import { userReducer, updateUser, updateTokens, clearUser, updateUserAndTokens } from './slices/userSlice';
import { settingReducer, updateSetting, clearSetting } from './slices/settingSlice';
import { authsApi } from './apis/authsApi';
import { settingsApi } from './apis/settingsApi';
import { usersApi } from './apis/usersApi';
import { heartbeatApi } from './apis/heartbeatApi';
import { phonesApi } from './apis/phonesApi';

export const store = configureStore({
  reducer: {
    setting: settingReducer,
    navbar: navbarReducer,
    signup: signUpReducer,
    signin: signInReducer,
    user: userReducer,
    [authsApi.reducerPath]: authsApi.reducer,
    [usersApi.reducerPath]: usersApi.reducer,
    [heartbeatApi.reducerPath]: heartbeatApi.reducer,
    [settingsApi.reducerPath]: settingsApi.reducer,
    [phonesApi.reducerPath]: phonesApi.reducer,
  },
  middleware: (getDefaultMiddleware) => {
    return getDefaultMiddleware()
      .concat(authsApi.middleware)
      .concat(usersApi.middleware)
      .concat(heartbeatApi.middleware)
      .concat(settingsApi.middleware)
      .concat(phonesApi.middleware);
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
  updateSetting,
  clearSetting,
};
export {
  useSignUpMutation,
  useSignInMutation,
  useSignOutMutation,
  useForgotPasswordMutation,
  useResetPasswordMutation,
  useFetchOTPQuery,
  useVerifyOTPMutation,
} from './apis/authsApi';
export { useSyncUserQuery, useUpdateUserPasswordMutation } from './apis/usersApi';
export { useFetchHeartBeatQuery, useLazyFetchHeartBeatQuery } from './apis/heartbeatApi';
export { useUpdateSettingsMFAMutation, useFetchSettingsQuery } from './apis/settingsApi';
export { useCreatePhoneMutation, useFetchPhoneQuery, useDeletePhoneMutation } from './apis/phonesApi';
export { authsApi, heartbeatApi, settingsApi, phonesApi };
