import { configureStore } from '@reduxjs/toolkit';
import { setupListeners } from '@reduxjs/toolkit/dist/query';
import { navbarReducer, openMobile, closeMobile } from './slices/navbarSlice';
import { signUpReducer, updateSignUpField, updateRole, clearSignUpForm } from './slices/signupSlice';
import { updateSignInField, clearSignInForm, signInReducer } from './slices/signinSlice';
import { userReducer, updateUser, updateTokens, clearUser, updateUserAndTokens } from './slices/userSlice';
import { settingReducer, updateSetting, clearSetting } from './slices/settingSlice';
import { updateBasicInfoFormField, clearBasicInfoForm, basicInfoFormReducer } from './slices/basicInfoFormSlice';
import { removeFromList, clearSkills, skillsFormReducer, addToList } from './slices/skillsFormSlice';
import {
  updateProfileSetupFormField,
  clearProfileSetupForm,
  updateAvatar,
  profileSetupFormReducer,
} from './slices/profileSetupFormSlice';
import { authsApi } from './apis/authsApi';
import { settingsApi } from './apis/settingsApi';
import { usersApi } from './apis/usersApi';
import { heartbeatApi } from './apis/heartbeatApi';
import { phonesApi } from './apis/phonesApi';
import { locationsApi } from './apis/locationsApi';
import { profilesApi } from './apis/profilesApi';

export const store = configureStore({
  reducer: {
    skills: skillsFormReducer,
    profileSetup: profileSetupFormReducer,
    basicInfo: basicInfoFormReducer,
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
    [locationsApi.reducerPath]: locationsApi.reducer,
    [profilesApi.reducerPath]: profilesApi.reducer,
  },
  middleware: (getDefaultMiddleware) => {
    return getDefaultMiddleware({
      serializableCheck: {
        ignoredActions: ['profileSetup/updateAvatar'],
        ignoredPaths: ['profileSetup.avatar.value'],
      },
    })
      .concat(authsApi.middleware)
      .concat(usersApi.middleware)
      .concat(heartbeatApi.middleware)
      .concat(settingsApi.middleware)
      .concat(phonesApi.middleware)
      .concat(locationsApi.middleware)
      .concat(profilesApi.middleware);
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
  clearBasicInfoForm,
  updateBasicInfoFormField,
  updateAvatar,
  clearProfileSetupForm,
  updateProfileSetupFormField,
  clearSkills,
  addToList,
  removeFromList,
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
export {
  useSyncUserQuery,
  useUpdateUserMutation,
  useUpdateUserPasswordMutation,
  useDeleteUserMutation,
} from './apis/usersApi';
export { useCreateAvatarMutation, useRemoveAvatarMutation } from './apis/profilesApi';
export { useFetchHeartBeatQuery, useLazyFetchHeartBeatQuery } from './apis/heartbeatApi';
export { useUpdateSettingsMFAMutation, useFetchSettingsQuery } from './apis/settingsApi';
export { useCreatePhoneMutation, useFetchPhoneQuery, useDeletePhoneMutation } from './apis/phonesApi';
export {
  useLazyFetchLocationsQuery,
  useCreateLocationMutation,
  useFetchSingleLocationQuery,
} from './apis/locationsApi';
export { authsApi, heartbeatApi, settingsApi, phonesApi, locationsApi, profilesApi };
