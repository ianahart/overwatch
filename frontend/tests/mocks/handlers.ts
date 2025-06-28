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

export const handlers = [
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
