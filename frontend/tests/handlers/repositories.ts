import { http, HttpResponse } from 'msw';
import { toPlainObject } from 'lodash';
import { baseURL } from '../../src/util';
import {
  ICreateRepositoryFileResponse,
  ICreateUserRepositoryResponse,
  IDeleteUserRepositoryResponse,
  IFetchDistinctRepositoryLanguagesResponse,
  IFetchRepositoriesResponse,
  IFetchRepositoryResponse,
  IFetchSearchRepositoryResponse,
  IFetchUserCommentRepositoryResponse,
  IGitHubRepository,
  IGitHubTree,
  IUpdateRepositoryCommentResponse,
  IUpdateRepositoryResponse,
  IUpdateRepositoryReviewStartTimeResponse,
} from '../../src/interfaces';
import { paginate } from '../utils';
import { createRepositories } from '../mocks/dbActions';
import { db } from '../mocks/db';

export const updateReviewStartTimeSpy = vi.fn();

const getRepoTreeData = () => {
  const languages = ['JavaScript', 'Python', 'Java'];
  const tree: IGitHubTree[] = [
    {
      path: 'README.md',
      sha: '3b18e8a9c0b0a6f6d3fa738d3f4ea7bd829d4e9d',
      size: 1200,
      type: 'blob',
      url: 'https://api.github.com/repos/example/repo/git/blobs/3b18e8a9c0b0a6f6d3fa738d3f4ea7bd829d4e9d',
    },
    {
      path: 'src/index.ts',
      sha: '5e6a2a0f9a7c62228dbab5b8e9b4fc0a4d1be30c',
      size: 580,
      type: 'blob',
      url: 'https://api.github.com/repos/example/repo/git/blobs/5e6a2a0f9a7c62228dbab5b8e9b4fc0a4d1be30c',
    },
  ];
  const repository: IGitHubRepository = { ...toPlainObject(db.gitHubRepository.create()), reviewerId: 1, ownerId: 1 };

  return { languages, tree, repository };
};

export const repositoriesHandlers = [
  http.get(`${baseURL}/repositories/search`, () => {
    const { languages, tree, repository } = getRepoTreeData();

    return HttpResponse.json<IFetchSearchRepositoryResponse>(
      {
        mesasge: 'success',
        data: {
          contents: {
            languages,
            tree,
          },
          repository,
        },
      },
      { status: 200 }
    );
  }),

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

  http.patch(`${baseURL}/repositories/:repositoryId/starttime`, () => {
    updateReviewStartTimeSpy();
    return HttpResponse.json<IUpdateRepositoryReviewStartTimeResponse>(
      {
        message: 'success',
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

  http.patch(`${baseURL}/repositories/:repositoryId`, () => {
    return HttpResponse.json<IUpdateRepositoryResponse>(
      {
        message: 'success',
        data: { status: 'COMPLETED', feedback: 'feedback' },
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

  http.get(`${baseURL}/repositories/:repositoryId`, () => {
    const { languages, tree, repository } = getRepoTreeData();

    return HttpResponse.json<IFetchRepositoryResponse>(
      {
        message: 'sucess',
        data: {
          contents: {
            languages,
            tree,
          },
          repository,
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
  http.post(`${baseURL}/repositories/file`, () => {
    return HttpResponse.json<ICreateRepositoryFileResponse>(
      {
        message: 'success',
        data: 'data',
      },
      { status: 201 }
    );
  }),
];
