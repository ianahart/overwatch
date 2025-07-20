import { http, HttpResponse } from 'msw';
import {
  ICreateFeedbackTemplateResponse,
  IDeleteFeedbackTemplateResponse,
  IGetAllFeedbackTemplateResponse,
  IGetFeedbackTemplateResponse,
} from '../../src/interfaces';
import { baseURL } from '../../src/util';

export const feedbackTemplatesHandlers = [
  http.delete(`${baseURL}/feedback-templates/:feedbackTemplateId`, () => {
    return HttpResponse.json<IDeleteFeedbackTemplateResponse>(
      {
        message: 'success',
      },
      { status: 200 }
    );
  }),

  http.get(`${baseURL}/feedback-templates`, () => {
    return HttpResponse.json<IGetAllFeedbackTemplateResponse>(
      {
        message: 'success',
        data: [{id: 1, userId: 1}, {id: 2, userId: 1}],
      },
      { status: 200 }
    );
  }),

  http.get(`${baseURL}/feedback-templates/:feedbackTemplateId`, () => {
    return HttpResponse.json<IGetFeedbackTemplateResponse>(
      {
        message: 'success',
        data: { id: 1, userId: 1, feedback: 'feedback' },
      },
      { status: 200 }
    );
  }),

  http.post(`${baseURL}/feedback-templates`, () => {
    return HttpResponse.json<ICreateFeedbackTemplateResponse>(
      {
        message: 'success',
      },
      { status: 201 }
    );
  }),
];
