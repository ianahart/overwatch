import { configureStore } from '@reduxjs/toolkit';
import { setupListeners } from '@reduxjs/toolkit/dist/query';
import { navbarReducer, openMobile, closeMobile } from './slices/navbarSlice';
import { signUpReducer, updateSignUpField, updateRole, clearSignUpForm } from './slices/signupSlice';
import { updateSignInField, clearSignInForm, signInReducer } from './slices/signinSlice';
import { userReducer, updateUser, updateTokens, clearUser, updateUserAndTokens } from './slices/userSlice';
import { settingReducer, updateSetting, clearSetting } from './slices/settingSlice';
import {
  updateBasicInfo,
  updateBasicInfoFormField,
  clearBasicInfoForm,
  basicInfoFormReducer,
} from './slices/basicInfoFormSlice';
import { updateSkills, removeFromList, clearSkills, skillsFormReducer, addToList } from './slices/skillsFormSlice';
import {
  updateAdditionalInfo,
  updateMoreInfo,
  addTimeSlot,
  removeTimeSlot,
  clearAdditionalInfoForm,
  additionalInfoFormReducer,
} from './slices/additionalInfoFormSlice';
import {
  updatePackagePrice,
  updatePackages,
  removePackageDesc,
  removePackageItem,
  updatePackageItem,
  addPackageItem,
  updatePackageDesc,
  clearPackageForm,
  packageFormReducer,
} from './slices/packageFormSlice';
import {
  updateWorkExp,
  removeWorkExpFromList,
  addWorkExpToList,
  clearWorkExpForm,
  workExpFormReducer,
} from './slices/workExpFormSlice';
import {
  updateProfileSetup,
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
import { paymentMethodsApi } from './apis/paymentMethodsApi';
import { testimonialsApi } from './apis/testimonialsApi';

export const store = configureStore({
  reducer: {
    additionalInfo: additionalInfoFormReducer,
    package: packageFormReducer,
    workExp: workExpFormReducer,
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
    [paymentMethodsApi.reducerPath]: paymentMethodsApi.reducer,
    [testimonialsApi.reducerPath]: testimonialsApi.reducer,
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
      .concat(profilesApi.middleware)
      .concat(paymentMethodsApi.middleware)
      .concat(testimonialsApi.middleware);
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
  clearWorkExpForm,
  addWorkExpToList,
  removeWorkExpFromList,
  clearPackageForm,
  updatePackageDesc,
  addPackageItem,
  updatePackageItem,
  removePackageItem,
  removePackageDesc,
  clearAdditionalInfoForm,
  addTimeSlot,
  removeTimeSlot,
  updateMoreInfo,
  updateBasicInfo,
  updateProfileSetup,
  updateSkills,
  updateWorkExp,
  updatePackages,
  updateAdditionalInfo,
  updatePackagePrice,
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
export {
  useFetchProfileQuery,
  useFetchPopulateProfileQuery,
  useUpdateProfileMutation,
  useCreateAvatarMutation,
  useRemoveAvatarMutation,
} from './apis/profilesApi';
export {
  useFetchPaymentMethodQuery,
  useCreatePaymentMethodMutation,
  useDeletePaymentMethodMutation,
} from './apis/paymentMethodsApi';
export { useFetchHeartBeatQuery, useLazyFetchHeartBeatQuery } from './apis/heartbeatApi';
export { useUpdateSettingsMFAMutation, useFetchSettingsQuery } from './apis/settingsApi';
export { useCreatePhoneMutation, useFetchPhoneQuery, useDeletePhoneMutation } from './apis/phonesApi';
export {
  useLazyFetchLocationsQuery,
  useCreateLocationMutation,
  useFetchSingleLocationQuery,
} from './apis/locationsApi';
export {
  useDeleteTestimonialMutation,
  useFetchTestimonialsQuery,
  useLazyFetchTestimonialsQuery,
  useCreateTestimonialMutation,
} from './apis/testimonialsApi';
export {
  testimonialsApi,
  authsApi,
  heartbeatApi,
  settingsApi,
  phonesApi,
  locationsApi,
  profilesApi,
  paymentMethodsApi,
};
