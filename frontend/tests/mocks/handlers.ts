import { authHandlers } from '../handlers/auth';
import { topicHandlers } from '../handlers/topics';
import { savedCommentHandlers } from '../handlers/savedComments';
import { profileHandlers } from '../handlers/profiles';
import { favoriteHandlers } from '../handlers/favorites';
import { notificationHandlers } from '../handlers/notifications';
import { replyCommentHandlers } from '../handlers/replyComments';

export const handlers = [
  ...notificationHandlers,
  ...authHandlers,
  ...topicHandlers,
  ...savedCommentHandlers,
  ...profileHandlers,
  ...favoriteHandlers,
  ...replyCommentHandlers,
];
