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

export const handlers = [
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
