import { useEffect, useState } from 'react';
import { Session } from '../../../../../util/SessionService';
import { useFetchGitHubUserReposQuery } from '../../../../../state/store';
import { retrieveTokens } from '../../../../../util';
import { IGitHubRepositoryPreview } from '../../../../../interfaces';
import Repository from './Repository';
import { useLazyFetchGitHubUserReposQuery } from '../../../../../state/apis/githubApi';

const RepositoryList = () => {
  const accessToken = Session.getItem('github_access_token') ?? '';
  const token = retrieveTokens().token;
  const [page, setPage] = useState(1);
  const [nextPageUrl, setNextPageUrl] = useState('');
  const { data } = useFetchGitHubUserReposQuery({ token, accessToken, page });
  const [paginateRepositories] = useLazyFetchGitHubUserReposQuery();
  const [repositories, setRepositories] = useState<IGitHubRepositoryPreview[]>([]);

  useEffect(() => {
    if (data !== undefined) {
      setRepositories(data.data.repositories);
      setNextPageUrl(data.data.nextPageUrl);
    }
  }, [data]);

  const handleOnClickMore = async () => {
    try {
      if (nextPageUrl === '') return;
      setPage((prevState) => prevState + 1);

      const response = await paginateRepositories({ token, page, accessToken }).unwrap();

      console.log(response);
      setRepositories((prevState) => [...prevState, ...response.data.repositories]);
    } catch (err) {
      console.log(err);
    }
  };

  return (
    <div className="overflow-scroll overflow-y-auto overflow-x-hidden h-[300px]">
      {repositories.map((repository) => {
        return <Repository key={repository.id} data={repository} />;
      })}
      {nextPageUrl.length > 0 && (
        <div className="flex justify-center">
          <button onClick={handleOnClickMore}>More repositories...</button>
        </div>
      )}
    </div>
  );
};

export default RepositoryList;
