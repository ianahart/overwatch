import { http, HttpResponse } from 'msw';
import { baseURL } from '../../src/util';
import { IFetchGitHubUserReposResponse } from '../../src/interfaces';
import { createGitHubRepositoryPreviews } from '../mocks/dbActions';

export const githubHandlers = [
  http.get(`${baseURL}/github/user/repos`, () => {
    const repositories = createGitHubRepositoryPreviews(2);
    const nextPageUrl = 'https://github.com/repos/page/1';

    return HttpResponse.json<IFetchGitHubUserReposResponse>(
      {
        message: 'success',
        data: {
          repositories: repositories,
          nextPageUrl,
        },
      },
      { status: 200 }
    );
  }),
];
