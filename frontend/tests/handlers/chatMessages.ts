import { http, HttpResponse } from 'msw';

import { IFetchChatMessagesResponse } from '../../src/interfaces';
import { baseURL } from '../../src/util';
import { createMessages } from '../mocks/dbActions';

export const chatMessagesHandlers = [
  http.get(`${baseURL}/chat-messages`, () => {
    const messages = createMessages(2);

    return HttpResponse.json<IFetchChatMessagesResponse>(
      {
        message: 'success',
        data: messages,
      },
      { status: 200 }
    );
  }),
];
