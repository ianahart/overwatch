import { configureStore } from '@reduxjs/toolkit';
import { setupListeners } from '@reduxjs/toolkit/dist/query';
import { navbarReducer, openMobile, closeMobile } from './slices/navbarSlice';
import { signUpReducer, updateSignUpField, updateRole, clearSignUpForm } from './slices/signupSlice';
import { updateSignInField, clearSignInForm, signInReducer } from './slices/signinSlice';
import { userReducer, updateUser, updateTokens, clearUser, updateUserAndTokens } from './slices/userSlice';
import { settingReducer, updateSetting, clearSetting } from './slices/settingSlice';
import {
  setRepositoryReviews,
  setRepositoryPagination,
  setRepositorySearchFilter,
  clearRepositoryReviews,
  repositoryReviewsReducer,
} from './slices/repositoryReviewsSlice';
import {
  clearAddReview,
  clearSelectedReviewer,
  clearReviewers,
  setReviewers,
  setSelectedReviewer,
  addReviewReducer,
} from './slices/addReviewSlice';
import {
  setMessages,
  addMessage,
  setConnections,
  setCurrentConnection,
  clearChat,
  chatReducer,
  clearMessages,
  removeConnection,
  setPinnedConnections,
  removePinnedConnection,
  clearPinnedConnections,
  clearConnections,
} from './slices/chatSlice';
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
import { reviewsApi } from './apis/reviewsApi';
import { connectionsApi } from './apis/connectionsApi';
import { notificationsApi } from './apis/notificationsApi';
import { chatMessagesApi } from './apis/chatMessagesApi';
import { favoritesApi } from './apis/favoritesApi';
import { pinnedConnectionsApi } from './apis/pinnedConnectionsApi';
import { githubApi } from './apis/githubApi';
import { repositoriesApi } from './apis/repositoriesApi';

export const store = configureStore({
  reducer: {
    repositoryReviews: repositoryReviewsReducer,
    addReview: addReviewReducer,
    chat: chatReducer,
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
    [reviewsApi.reducerPath]: reviewsApi.reducer,
    [connectionsApi.reducerPath]: connectionsApi.reducer,
    [notificationsApi.reducerPath]: notificationsApi.reducer,
    [chatMessagesApi.reducerPath]: chatMessagesApi.reducer,
    [favoritesApi.reducerPath]: favoritesApi.reducer,
    [pinnedConnectionsApi.reducerPath]: pinnedConnectionsApi.reducer,
    [githubApi.reducerPath]: githubApi.reducer,
    [repositoriesApi.reducerPath]: repositoriesApi.reducer,
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
      .concat(testimonialsApi.middleware)
      .concat(reviewsApi.middleware)
      .concat(connectionsApi.middleware)
      .concat(notificationsApi.middleware)
      .concat(chatMessagesApi.middleware)
      .concat(favoritesApi.middleware)
      .concat(pinnedConnectionsApi.middleware)
      .concat(githubApi.middleware)
      .concat(repositoriesApi.middleware);
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
  clearChat,
  setCurrentConnection,
  setConnections,
  setMessages,
  addMessage,
  clearMessages,
  removeConnection,
  setPinnedConnections,
  removePinnedConnection,
  clearPinnedConnections,
  clearConnections,
  clearAddReview,
  clearSelectedReviewer,
  clearReviewers,
  setReviewers,
  setSelectedReviewer,
  clearRepositoryReviews,
  setRepositorySearchFilter,
  setRepositoryReviews,
  setRepositoryPagination,
};

export {
  useDeletePinnedConnectionMutation,
  useLazyFetchPinnedConnectionsQuery,
  useFetchPinnedConnectionsQuery,
  useCreatePinnedConnectionMutation,
} from './apis/pinnedConnectionsApi';

export { useToggleFavoriteMutation } from './apis/favoritesApi';

export { useLazyFetchChatMessagesQuery } from './apis/chatMessagesApi';

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
  useFetchAllProfileQuery,
  useLazyFetchAllProfileQuery,
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
  useFetchTopTestimonialsQuery,
  useDeleteTestimonialMutation,
  useFetchTestimonialsQuery,
  useLazyFetchTestimonialsQuery,
  useCreateTestimonialMutation,
} from './apis/testimonialsApi';
export {
  useFetchReviewQuery,
  useCreateReviewMutation,
  useLazyFetchReviewsQuery,
  useFetchReviewsQuery,
  useEditReviewMutation,
  useDeleteReviewMutation,
} from './apis/reviewsApi';

export {
  useLazyFetchSearchConnectionsQuery,
  useFetchConnectionsQuery,
  useLazyFetchConnectionsQuery,
  useDeleteConnectionMutation,
  useCreateConnectionMutation,
  useVerifyConnectionQuery,
} from './apis/connectionsApi';

export {
  useDeleteNotificationMutation,
  useFetchNotificationsQuery,
  useLazyFetchNotificationsQuery,
} from './apis/notificationsApi';

export {
  useLazyFetchGitHubAccessTokenQuery,
  useFetchGitHubUserReposQuery,
  useLazyFetchGitHubUserReposQuery,
} from './apis/githubApi';

export {
  useUpdateRepositoryCommentMutation,
  useFetchUserCommentRepositoryQuery,
  useDeleteUserRepositoryMutation,
  useFetchDistinctRepositoryLanguagesQuery,
  useCreateUserRepositoryMutation,
  useFetchRepositoriesQuery,
  useLazyFetchRepositoriesQuery,
} from './apis/repositoriesApi';

export {
  testimonialsApi,
  authsApi,
  heartbeatApi,
  settingsApi,
  phonesApi,
  locationsApi,
  profilesApi,
  paymentMethodsApi,
  reviewsApi,
  connectionsApi,
  notificationsApi,
  pinnedConnectionsApi,
  githubApi,
  repositoriesApi,
};
