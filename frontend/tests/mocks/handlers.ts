import { authHandlers } from '../handlers/auth';
import { topicHandlers } from '../handlers/topics';
import { savedCommentHandlers } from '../handlers/savedComments';

export const handlers = [...authHandlers, ...topicHandlers, ...savedCommentHandlers];
