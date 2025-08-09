import { authHandlers } from '../handlers/auth';
import { topicHandlers } from '../handlers/topics';
import { savedCommentHandlers } from '../handlers/savedComments';
import { profileHandlers } from '../handlers/profiles';
import { favoriteHandlers } from '../handlers/favorites';
import { notificationHandlers } from '../handlers/notifications';
import { replyCommentHandlers } from '../handlers/replyComments';
import { commentHandlers } from '../handlers/comments';
import { reactionHandlers } from '../handlers/reactions';
import { reportCommentHandlers } from '../handlers/reportComments';
import { reviewsHandlers } from '../handlers/reviews';
import { connectionsHandlers } from '../handlers/connections';
import { testimonialsHandlers } from '../handlers/testimonials';
import { usersHandlers } from '../handlers/users';
import { teamsHandlers } from '../handlers/teams';
import { teamInvitationsHandlers } from '../handlers/teamInvitations';
import { teamMembersHandlers } from '../handlers/teamMembers';
import { teamMessagesHandlers } from '../handlers/teamMessages';
import { teamCommentsHandlers } from '../handlers/teamComments';
import { teamPostsHandlers } from '../handlers/teamPosts';
import { teamPinnedMessagesHandlers } from '../handlers/teamPinnedMessages';
import { settingsHandlers } from '../handlers/settings';
import { phonesHandlers } from '../handlers/phones';
import { blockedUsersHandlers } from '../handlers/blockedUsers';
import { repositoriesHandlers } from '../handlers/repositories';
import { paymentMethodsHandlers } from '../handlers/paymentMethods';
import { locationHandlers } from '../handlers/locations';
import { connectionPinsHandlers } from '../handlers/connectionPins';
import { chatMessagesHandlers } from '../handlers/chatMessages';
import { paymentRefundsHandlers } from '../handlers/paymentRefunds';
import { paymentIntentsHandlers } from '../handlers/paymentIntents';
import { suggestionsHandlers } from '../handlers/suggestions';
import { githubHandlers } from '../handlers/github';
import { appTestimonialsHandlers } from '../handlers/appTestimonials';
import { reviewFeedbacksHandlers } from '../handlers/reviewFeedbacks';
import { badgesHandlers } from '../handlers/badges';
import { feedbackTemplatesHandlers } from '../handlers/feedbackTemplates';
import { todoCardsHandlers } from '../handlers/todoCards';
import { todoListsHandlers } from '../handlers/todoLists';
import { activeLabelsHandlers } from '../handlers/activeLabels';
import { activitiesHandlers } from '../handlers/activities';
import { checkListsHandlers } from '../handlers/checkLists';
import { checkListsItemsHandlers } from '../handlers/checkListItems';
import { customFieldsHandlers } from '../handlers/customFields';

export const handlers = [
  ...customFieldsHandlers,
  ...checkListsItemsHandlers,
  ...checkListsHandlers,
  ...activeLabelsHandlers,
  ...activitiesHandlers,
  ...todoCardsHandlers,
  ...todoListsHandlers,
  ...feedbackTemplatesHandlers,
  ...badgesHandlers,
  ...reviewFeedbacksHandlers,
  ...appTestimonialsHandlers,
  ...githubHandlers,
  ...suggestionsHandlers,
  ...paymentIntentsHandlers,
  ...paymentRefundsHandlers,
  ...chatMessagesHandlers,
  ...connectionPinsHandlers,
  ...locationHandlers,
  ...paymentMethodsHandlers,
  ...repositoriesHandlers,
  ...blockedUsersHandlers,
  ...phonesHandlers,
  ...settingsHandlers,
  ...teamPinnedMessagesHandlers,
  ...teamCommentsHandlers,
  ...teamPostsHandlers,
  ...teamMessagesHandlers,
  ...teamMembersHandlers,
  ...teamsHandlers,
  ...teamInvitationsHandlers,
  ...usersHandlers,
  ...testimonialsHandlers,
  ...connectionsHandlers,
  ...reviewsHandlers,
  ...reportCommentHandlers,
  ...reactionHandlers,
  ...commentHandlers,
  ...replyCommentHandlers,
  ...notificationHandlers,
  ...authHandlers,
  ...topicHandlers,
  ...savedCommentHandlers,
  ...profileHandlers,
  ...favoriteHandlers,
];
