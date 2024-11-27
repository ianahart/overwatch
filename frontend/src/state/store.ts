import { configureStore } from '@reduxjs/toolkit';
import { setupListeners } from '@reduxjs/toolkit/dist/query';
import { navbarReducer, openMobile, closeMobile } from './slices/navbarSlice';
import { signUpReducer, updateSignUpField, updateRole, clearSignUpForm } from './slices/signupSlice';
import { updateSignInField, clearSignInForm, signInReducer } from './slices/signinSlice';
import { userReducer, updateUser, updateTokens, clearUser, updateUserAndTokens } from './slices/userSlice';
import { settingReducer, updateSetting, clearSetting } from './slices/settingSlice';
import {
  teamReducer,
  clearAdminTeams,
  clearTeamPagination,
  setTeamPagination,
  setCurrentTeam,
  setAdminTeams,
  clearTeams,
} from './slices/teamSlice';
import {
  toggleLabel,
  setLabels,
  updateWorkSpaceProperty,
  clearWorkSpace,
  workSpaceReducer,
  setWorkSpace,
} from './slices/workSpaceSlice';
import {
  moveTodoCard,
  reorderTodoCards,
  removeTodoListTodoCard,
  updateTodoListTodoCard,
  addCardToTodoList,
  deleteSingleTodoList,
  updateSingleTodoList,
  clearTodoLists,
  addToTodoList,
  setTodoLists,
  todoListsReducer,
} from './slices/todoListSlice';
import {
  setRepositoryNavView,
  setRepositoryLanguages,
  setRepositoryFile,
  setRepository,
  setRepositoryTree,
  setRepositoryPage,
  repositoryTreeReducer,
  clearRepositoryTree,
  updateRepository,
} from './slices/repositoryTreeSlice';

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
import { workSpacesApi } from './apis/workSpacesApi';
import { todoListsApi } from './apis/todoListsApi';
import { todoCardsApi } from './apis/todoCardsApi';
import { pexelApi } from './apis/pexelsApi';
import { labelsApi } from './apis/labelsApi';
import { activeLabelsApi } from './apis/activeLabelsApi';
import { checkListsApi } from './apis/checkListApis';
import { checkListItemsApi } from './apis/checkListItemsApi';
import { activitiesApi } from './apis/activitiesApi';
import { customFieldsApi } from './apis/customFieldsApi';
import { dropDownOptionsApi } from './apis/dropDownOptionsApi';
import { reviewFeedbacksApi } from './apis/reviewFeedbacksApi';
import { statisticsApi } from './apis/statisticsApi';
import { blockedUsersApi } from './apis/blockedUsersApi';
import { topicsApi } from './apis/topicsApi';
import { commentsApi } from './apis/commentsApi';
import { commentVotesApi } from './apis/commentVotesApi';
import { reportCommentsApi } from './apis/reportedCommentsApi';
import { saveCommentsApi } from './apis/savedCommentsApi';
import { reactionsApi } from './apis/reactionsApi';
import { replyCommentsApi } from './apis/replyCommentsApi';
import { stripePaymentIntentsApi } from './apis/stripePaymentIntentsApi';
import { stripePaymentRefundsApi } from './apis/stripePaymentRefundsApi';
import { appTestimonialsApi } from './apis/appTestimonialsApi';
import { teamsApi } from './apis/teamsApi';
import { teamInvitationsApi } from './apis/teamInvitationsApi';

export const store = configureStore({
  reducer: {
    todoList: todoListsReducer,
    workSpace: workSpaceReducer,
    repositoryTree: repositoryTreeReducer,
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
    team: teamReducer,
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
    [workSpacesApi.reducerPath]: workSpacesApi.reducer,
    [todoListsApi.reducerPath]: todoListsApi.reducer,
    [todoCardsApi.reducerPath]: todoCardsApi.reducer,
    [pexelApi.reducerPath]: pexelApi.reducer,
    [labelsApi.reducerPath]: labelsApi.reducer,
    [activeLabelsApi.reducerPath]: activeLabelsApi.reducer,
    [checkListsApi.reducerPath]: checkListsApi.reducer,
    [checkListItemsApi.reducerPath]: checkListItemsApi.reducer,
    [activitiesApi.reducerPath]: activitiesApi.reducer,
    [customFieldsApi.reducerPath]: customFieldsApi.reducer,
    [dropDownOptionsApi.reducerPath]: dropDownOptionsApi.reducer,
    [reviewFeedbacksApi.reducerPath]: reviewFeedbacksApi.reducer,
    [statisticsApi.reducerPath]: statisticsApi.reducer,
    [blockedUsersApi.reducerPath]: blockedUsersApi.reducer,
    [topicsApi.reducerPath]: topicsApi.reducer,
    [commentsApi.reducerPath]: commentsApi.reducer,
    [commentVotesApi.reducerPath]: commentVotesApi.reducer,
    [reportCommentsApi.reducerPath]: reportCommentsApi.reducer,
    [saveCommentsApi.reducerPath]: saveCommentsApi.reducer,
    [reactionsApi.reducerPath]: reactionsApi.reducer,
    [replyCommentsApi.reducerPath]: replyCommentsApi.reducer,
    [stripePaymentIntentsApi.reducerPath]: stripePaymentIntentsApi.reducer,
    [stripePaymentRefundsApi.reducerPath]: stripePaymentRefundsApi.reducer,
    [appTestimonialsApi.reducerPath]: appTestimonialsApi.reducer,
    [teamsApi.reducerPath]: teamsApi.reducer,
    [teamInvitationsApi.reducerPath]: teamInvitationsApi.reducer,
  },
  middleware: (getDefaultMiddleware) => {
    return getDefaultMiddleware({
      serializableCheck: {
        ignoredActions: [
          'profileSetup/updateAvatar',
          'stripePaymentIntent/exportPaymentIntentsToPdf',
          'stripePaymentIntent/exportPaymentIntentsToCsv',
        ],
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
      .concat(repositoriesApi.middleware)
      .concat(workSpacesApi.middleware)
      .concat(todoListsApi.middleware)
      .concat(todoCardsApi.middleware)
      .concat(pexelApi.middleware)
      .concat(labelsApi.middleware)
      .concat(activeLabelsApi.middleware)
      .concat(checkListsApi.middleware)
      .concat(checkListItemsApi.middleware)
      .concat(activitiesApi.middleware)
      .concat(customFieldsApi.middleware)
      .concat(dropDownOptionsApi.middleware)
      .concat(reviewFeedbacksApi.middleware)
      .concat(statisticsApi.middleware)
      .concat(blockedUsersApi.middleware)
      .concat(topicsApi.middleware)
      .concat(commentsApi.middleware)
      .concat(commentVotesApi.middleware)
      .concat(reportCommentsApi.middleware)
      .concat(saveCommentsApi.middleware)
      .concat(reactionsApi.middleware)
      .concat(replyCommentsApi.middleware)
      .concat(stripePaymentIntentsApi.middleware)
      .concat(stripePaymentRefundsApi.middleware)
      .concat(appTestimonialsApi.middleware)
      .concat(teamsApi.middleware)
      .concat(teamInvitationsApi.middleware);
  },
});

setupListeners(store.dispatch);

export type TRootState = ReturnType<typeof store.getState>;
export type TAppDispatch = typeof store.dispatch;

export {
  clearAdminTeams,
  clearTeamPagination,
  setTeamPagination,
  setCurrentTeam,
  setAdminTeams,
  clearTeams,
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
  clearRepositoryTree,
  setRepository,
  setRepositoryPage,
  setRepositoryTree,
  setRepositoryFile,
  setRepositoryLanguages,
  setRepositoryNavView,
  updateRepository,
  clearWorkSpace,
  updateWorkSpaceProperty,
  setWorkSpace,
  setTodoLists,
  addToTodoList,
  clearTodoLists,
  updateSingleTodoList,
  deleteSingleTodoList,
  addCardToTodoList,
  updateTodoListTodoCard,
  removeTodoListTodoCard,
  moveTodoCard,
  reorderTodoCards,
  setLabels,
  toggleLabel,
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
  useLazyFetchReviewersQuery,
  useSyncUserQuery,
  useUpdateUserMutation,
  useUpdateUserPasswordMutation,
  useDeleteUserMutation,
} from './apis/usersApi';
export {
  useLazyFetchProfilePackagesQuery,
  useFetchProfileVisibilityQuery,
  useUpdateProfileVisibilityMutation,
  useFetchAllProfileQuery,
  useLazyFetchAllProfileQuery,
  useFetchProfileQuery,
  useFetchPopulateProfileQuery,
  useUpdateProfileMutation,
  useCreateAvatarMutation,
  useRemoveAvatarMutation,
} from './apis/profilesApi';
export {
  useTransferCustomerMoneyToReviewerMutation,
  useConnectAccountMutation,
  useFetchPaymentMethodQuery,
  useCreatePaymentMethodMutation,
  useDeletePaymentMethodMutation,
} from './apis/paymentMethodsApi';
export { useFetchHeartBeatQuery, useLazyFetchHeartBeatQuery } from './apis/heartbeatApi';
export { useUpdateSettingsMutation, useUpdateSettingsMFAMutation, useFetchSettingsQuery } from './apis/settingsApi';
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
  useLazyFetchRepositoryQuery,
  useFetchRepositoryQuery,
  useUpdateRepositoryCommentMutation,
  useFetchUserCommentRepositoryQuery,
  useDeleteUserRepositoryMutation,
  useFetchDistinctRepositoryLanguagesQuery,
  useCreateUserRepositoryMutation,
  useFetchRepositoriesQuery,
  useLazyFetchRepositoriesQuery,
  useCreateRepositoryFileMutation,
  useUpdateRepositoryReviewMutation,
  useUpdateRepositoryReviewStartTimeMutation,
} from './apis/repositoriesApi';

export {
  useFetchLatestWorkspaceQuery,
  useDeleteWorkSpaceMutation,
  useEditWorkSpaceMutation,
  useCreateWorkSpaceMutation,
  useFetchWorkspacesQuery,
  useLazyFetchWorkspacesQuery,
} from './apis/workSpacesApi';

export {
  useDeleteTodoListMutation,
  useCreateTodoListMutation,
  useLazyFetchTodoListsQuery,
  useFetchTodoListsQuery,
  useEditTodoListMutation,
  useReorderTodoListsMutation,
} from './apis/todoListsApi';

export {
  useUploadTodoCardPhotoMutation,
  useMoveTodoCardsMutation,
  useReorderTodoCardsMutation,
  useDeleteTodoCardMutation,
  useUpdateTodoCardMutation,
  useCreateTodoCardMutation,
} from './apis/todoCardsApi';

export { useLazyFetchPexelPhotosQuery, useFetchPexelPhotosQuery } from './apis/pexelsApi';

export {
  useUpdateLabelMutation,
  useDeleteLabelMutation,
  useFetchLabelsQuery,
  useCreateLabelMutation,
} from './apis/labelsApi';

export {
  useFetchActiveLabelsQuery,
  useCreateActiveLabelMutation,
  useDeleteActiveLabelMutation,
} from './apis/activeLabelsApi';

export { useDeleteCheckListMutation, useFetchCheckListsQuery, useCreateCheckListMutation } from './apis/checkListApis';

export {
  useDeleteCheckListItemMutation,
  useUpdateCheckListItemMutation,
  useCreateCheckListItemMutation,
} from './apis/checkListItemsApi';

export {
  useDeleteActivityMutation,
  useLazyFetchActivitiesQuery,
  useFetchActivitiesQuery,
  useCreateActivityMutation,
} from './apis/activitiesApi';

export {
  useUpdateCustomFieldMutation,
  useCreateCustomFieldMutation,
  useFetchCustomFieldsQuery,
  useDeleteCustomFieldMutation,
} from './apis/customFieldsApi';

export { useDeleteDropDownOptionMutation } from './apis/dropDownOptionsApi';

export { useLazyGetSingleReviewFeedbackQuery, useCreateReviewFeedbackMutation } from './apis/reviewFeedbacksApi';

export { useFetchStatisticsQuery } from './apis/statisticsApi';

export {
  useCreateBlockedUserMutation,
  useFetchBlockedUsersQuery,
  useLazyFetchBlockedUsersQuery,
  useDeleteBlockedUserMutation,
} from './apis/blockedUsersApi';

export {
  useUpdateTopicMutation,
  useLazyFetchUserTopicsQuery,
  useLazyFetchTopicsWithTagsQuery,
  useFetchTopicsQuery,
  useLazySearchTopicsQuery,
  useCreateTopicMutation,
  useFetchTopicQuery,
  useLazyFetchTopicsQuery,
} from './apis/topicsApi';

export {
  useFetchCommentQuery,
  useDeleteCommentMutation,
  useUpdateCommentMutation,
  useCreateCommentMutation,
  useFetchCommentsQuery,
  useLazyFetchCommentsQuery,
} from './apis/commentsApi';

export { useCreateVoteMutation } from './apis/commentVotesApi';

export {
  useDeleteReportCommentMutation,
  useLazyFetchReportCommentsQuery,
  useCreateReportCommentMutation,
} from './apis/reportedCommentsApi';

export {
  useDeleteSaveCommentMutation,
  useLazyFetchSaveCommentsQuery,
  useCreateSaveCommentMutation,
} from './apis/savedCommentsApi';

export { useDeleteReactionMutation, useCreateReactionMutation, useFetchReactionQuery } from './apis/reactionsApi';

export {
  useDeleteReplyCommentMutation,
  useUpdateReplyCommentMutation,
  useLazyFetchReplyCommentsQuery,
  useLazyFetchReplyCommentsByUserQuery,
  useCreateReplyCommentMutation,
} from './apis/replyCommentsApi';

export {
  useLazyExportPaymentIntentsToCsvQuery,
  useLazyExportPaymentIntentsToPdfQuery,
  useLazyFetchUserPaymentIntentsQuery,
  useLazyFetchAllPaymentIntentsQuery,
} from './apis/stripePaymentIntentsApi';

export {
  useUpdatePaymentRefundMutation,
  useLazyFetchPaymentRefundsQuery,
  useCreatePaymentRefundMutation,
} from './apis/stripePaymentRefundsApi';

export {
  useFetchAppTestimonialsQuery,
  useDeleteAppTestimonialMutation,
  useUpdateAppTestimonialMutation,
  useFetchAppTestimonialQuery,
  useCreateAppTestimonialMutation,
} from './apis/appTestimonialsApi';

export { useFetchTeamsQuery, useLazyFetchTeamsQuery, useCreateTeamMutation } from './apis/teamsApi';

export {
  useDeleteTeamInvitationMutation,
  useFetchTeamInvitationsQuery,
  useLazyFetchTeamInvitationsQuery,
  useCreateTeamInvitationMutation,
} from './apis/teamInvitationsApi';

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
  workSpacesApi,
  todoListsApi,
  pexelApi,
  labelsApi,
  activeLabelsApi,
  activitiesApi,
  customFieldsApi,
  dropDownOptionsApi,
  reviewFeedbacksApi,
  statisticsApi,
  blockedUsersApi,
  topicsApi,
  commentsApi,
  commentVotesApi,
  reportCommentsApi,
  saveCommentsApi,
  reactionsApi,
  replyCommentsApi,
  stripePaymentIntentsApi,
  stripePaymentRefundsApi,
  appTestimonialsApi,
  teamsApi,
  teamInvitationsApi,
};
