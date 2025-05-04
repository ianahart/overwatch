import { ReactNode } from 'react';
import { Provider } from 'react-redux';
import { configureStore, PreloadedState } from '@reduxjs/toolkit';
import { rootReducer, TRootState } from '../src/state/store';
import { authsApi } from '../src/state/apis/authsApi';
import { settingsApi } from '../src/state/apis/settingsApi';
import { usersApi } from '../src/state/apis/usersApi';
import { heartbeatApi } from '../src/state/apis/heartbeatApi';
import { phonesApi } from '../src/state/apis/phonesApi';
import { locationsApi } from '../src/state/apis/locationsApi';
import { profilesApi } from '../src/state/apis/profilesApi';
import { paymentMethodsApi } from '../src/state/apis/paymentMethodsApi';
import { testimonialsApi } from '../src/state/apis/testimonialsApi';
import { reviewsApi } from '../src/state/apis/reviewsApi';
import { connectionsApi } from '../src/state/apis/connectionsApi';
import { notificationsApi } from '../src/state/apis/notificationsApi';
import { chatMessagesApi } from '../src/state/apis/chatMessagesApi';
import { favoritesApi } from '../src/state/apis/favoritesApi';
import { pinnedConnectionsApi } from '../src/state/apis/pinnedConnectionsApi';
import { githubApi } from '../src/state/apis/githubApi';
import { repositoriesApi } from '../src/state/apis/repositoriesApi';
import { workSpacesApi } from '../src/state/apis/workSpacesApi';
import { todoListsApi } from '../src/state/apis/todoListsApi';
import { todoCardsApi } from '../src/state/apis/todoCardsApi';
import { pexelApi } from '../src/state/apis/pexelsApi';
import { labelsApi } from '../src/state/apis/labelsApi';
import { activeLabelsApi } from '../src/state/apis/activeLabelsApi';
import { checkListsApi } from '../src/state/apis/checkListApis';
import { checkListItemsApi } from '../src/state/apis/checkListItemsApi';
import { activitiesApi } from '../src/state/apis/activitiesApi';
import { customFieldsApi } from '../src/state/apis/customFieldsApi';
import { dropDownOptionsApi } from '../src/state/apis/dropDownOptionsApi';
import { reviewFeedbacksApi } from '../src/state/apis/reviewFeedbacksApi';
import { statisticsApi } from '../src/state/apis/statisticsApi';
import { blockedUsersApi } from '../src/state/apis/blockedUsersApi';
import { topicsApi } from '../src/state/apis/topicsApi';
import { commentsApi } from '../src/state/apis/commentsApi';
import { commentVotesApi } from '../src/state/apis/commentVotesApi';
import { reportCommentsApi } from '../src/state/apis/reportedCommentsApi';
import { saveCommentsApi } from '../src/state/apis/savedCommentsApi';
import { reactionsApi } from '../src/state/apis/reactionsApi';
import { replyCommentsApi } from '../src/state/apis/replyCommentsApi';
import { stripePaymentIntentsApi } from '../src/state/apis/stripePaymentIntentsApi';
import { stripePaymentRefundsApi } from '../src/state/apis/stripePaymentRefundsApi';
import { appTestimonialsApi } from '../src/state/apis/appTestimonialsApi';
import { teamsApi } from '../src/state/apis/teamsApi';
import { teamInvitationsApi } from '../src/state/apis/teamInvitationsApi';
import { teamMembersApi } from '../src/state/apis/teamMembersApi';
import { teamMessagesApi } from '../src/state/apis/teamMessagesApi';
import { teamPostsApi } from '../src/state/apis/teamPostsApi';
import { teamCommentsApi } from '../src/state/apis/teamCommentsApi';
import { bannedUsersApi } from '../src/state/apis/bannedUsersApi';
import { suggestionsApi } from '../src/state/apis/suggestionsApi';
import { badgesApi } from '../src/state/apis/badgesApi';
import { reviewerBadgesApi } from '../src/state/apis/reviewerBadgesApi';
import { feedbackTemplatesApi } from '../src/state/apis/feedbackTemplatesApi';
import { teamPinnedMessagesApi } from '../src/state/apis/teamPinnedMessagesApi';
import { MemoryRouter } from 'react-router-dom';

interface AllProvidersProps {
  children: ReactNode;
  preloadedState?: PreloadedState<TRootState>;
}

export function AllProviders({ children, preloadedState }: AllProvidersProps) {
  const store = configureStore({
    reducer: rootReducer,
    preloadedState,
    middleware: (getDefaultMiddleware) =>
      getDefaultMiddleware({
        serializableCheck: {
          ignoredActions: [
            'profileSetup/updateAvatar',
            'stripePaymentIntent/exportPaymentIntentsToPdf',
            'stripePaymentIntent/exportPaymentIntentsToCsv',
            'suggestions/createSuggestion',
            'badges/createBadge',
          ],
          ignoredPaths: ['profileSetup.avatar.value'],
        },
      }).concat(
        authsApi.middleware,
        usersApi.middleware,
        heartbeatApi.middleware,
        settingsApi.middleware,
        phonesApi.middleware,
        locationsApi.middleware,
        profilesApi.middleware,
        paymentMethodsApi.middleware,
        testimonialsApi.middleware,
        reviewsApi.middleware,
        connectionsApi.middleware,
        notificationsApi.middleware,
        chatMessagesApi.middleware,
        favoritesApi.middleware,
        pinnedConnectionsApi.middleware,
        githubApi.middleware,
        repositoriesApi.middleware,
        workSpacesApi.middleware,
        todoListsApi.middleware,
        todoCardsApi.middleware,
        pexelApi.middleware,
        labelsApi.middleware,
        activeLabelsApi.middleware,
        checkListsApi.middleware,
        checkListItemsApi.middleware,
        activitiesApi.middleware,
        customFieldsApi.middleware,
        dropDownOptionsApi.middleware,
        reviewFeedbacksApi.middleware,
        statisticsApi.middleware,
        blockedUsersApi.middleware,
        topicsApi.middleware,
        commentsApi.middleware,
        commentVotesApi.middleware,
        reportCommentsApi.middleware,
        saveCommentsApi.middleware,
        reactionsApi.middleware,
        replyCommentsApi.middleware,
        stripePaymentIntentsApi.middleware,
        stripePaymentRefundsApi.middleware,
        appTestimonialsApi.middleware,
        teamsApi.middleware,
        teamInvitationsApi.middleware,
        teamMembersApi.middleware,
        teamMessagesApi.middleware,
        teamPostsApi.middleware,
        teamCommentsApi.middleware,
        bannedUsersApi.middleware,
        suggestionsApi.middleware,
        badgesApi.middleware,
        reviewerBadgesApi.middleware,
        feedbackTemplatesApi.middleware,
        teamPinnedMessagesApi.middleware
      ),
  });

  return (
    <Provider store={store}>
      <MemoryRouter>{children}</MemoryRouter>
    </Provider>
  );
}
