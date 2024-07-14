import { useEffect, useRef } from 'react';
import { Session } from '../../../../../util/SessionService';
import { useFetchGitHubUserReposQuery } from '../../../../../state/store';
import { retrieveTokens } from '../../../../../util';

const RepositoryList = () => {
  const [repositories]
  const accessToken = Session.getItem('github_access_token') ?? '';
  const token = retrieveTokens().token;
  const { data } = useFetchGitHubUserReposQuery({ token, accessToken });

  console.log(data);

  return <div>Repositories</div>;
};

export default Repositories;
