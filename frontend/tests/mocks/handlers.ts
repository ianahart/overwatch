import { authHandlers } from '../handlers/auth';
import { topicHandlers } from '../handlers/topics';
import { savedCommentHandlers } from '../handlers/savedComments';
import { profileHandlers } from '../handlers/profiles';
import { favoriteHandlers } from '../handlers/favorites';

export const handlers = [
  ...authHandlers,
  ...topicHandlers,
  ...savedCommentHandlers,
  ...profileHandlers,
  ...favoriteHandlers,
];
