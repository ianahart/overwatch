import { http, HttpResponse } from 'msw';
import { baseURL } from '../../src/util';
import {
  ICreateUserRepositoryResponse,
  IDeleteUserRepositoryResponse,
  IFetchDistinctRepositoryLanguagesResponse,
  IFetchRepositoriesResponse,
  IFetchUserCommentRepositoryResponse,
  IUpdateRepositoryCommentResponse,
} from '../../src/interfaces';
import { paginate } from '../utils';
import { createRepositories } from '../mocks/dbActions';

export const repositoriesHandlers = [
  http.get(`${baseURL}/repositories/languages`, () => {
    const languages = ['JavaScript', 'CSS', 'HTML'];

    return HttpResponse.json<IFetchDistinctRepositoryLanguagesResponse>(
      {
        message: 'success',
        data: languages,
      },
      { status: 200 }
    );
  }),

  http.get(`${baseURL}/repositories/:repositoryId/comment`, () => {
    return HttpResponse.json<IFetchUserCommentRepositoryResponse>(
      {
        message: 'success',
        data: 'existing repository comment',
      },
      { status: 200 }
    );
  }),

  http.patch(`${baseURL}/repositories/:repositoryId/comment`, () => {
    return HttpResponse.json<IUpdateRepositoryCommentResponse>(
      {
        message: 'success',
      },
      { status: 200 }
    );
  }),

  http.post(`${baseURL}/repositories/user`, () => {
    return HttpResponse.json<ICreateUserRepositoryResponse>(
      {
        message: 'success',
      },
      { status: 201 }
    );
  }),

  http.get(`${baseURL}/repositories`, async ({ request }) => {
    const url = new URL(request.url);

    let pg = Number(url.searchParams.get('page')) ?? 1;
    const size = 2;
    const dir = url.searchParams.get('direction') ?? 'next';
    const totalElements = 4;

    const data = createRepositories(totalElements, { status: 'COMPLETED' });

    const { page, totalPages, pageSize, direction, items } = paginate(pg, size, dir, data);

    return HttpResponse.json<IFetchRepositoriesResponse>(
      {
        message: 'success',
        data: {
          items,
          page,
          pageSize,
          totalPages,
          direction,
          totalElements,
        },
      },
      { status: 200 }
    );
  }),

  http.delete(`${baseURL}/repositories/:repositoryId`, () => {
    return HttpResponse.json<IDeleteUserRepositoryResponse>(
      {
        message: 'success',
      },
      { status: 200 }
    );
  }),
];
