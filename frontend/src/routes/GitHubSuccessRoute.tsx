import { useEffect } from 'react';
import { useSearchParams, useNavigate } from 'react-router-dom';
import { TRootState, useLazyFetchGitHubAccessTokenQuery } from '../state/store';
import { useSelector } from 'react-redux';
import { retrieveTokens } from '../util';

const GitHubSuccessRoute = () => {
  const navigate = useNavigate();
  const [searchParams, _] = useSearchParams();
  const [fetchGitHubAccessToken] = useLazyFetchGitHubAccessTokenQuery();
  const { user } = useSelector((store: TRootState) => store.user);
  const token = retrieveTokens()?.token;

  useEffect(() => {
    if (searchParams.has('code') && token && user.slug) {
      const code = searchParams.get('code');
      if (code) {
        const makeReq = async () => {
          try {
            const response = await fetchGitHubAccessToken({ code, token }).unwrap();
            const { accessToken } = response;
            navigate(`/dashboard/${user.slug}/user/add-review?verified=true`, { state: { accessToken } });
          } catch (err) {
            console.log(err);
          }
        };
        makeReq();
      }
    }
  }, [searchParams, token, user.slug, navigate]);

  return <div></div>;
};

export default GitHubSuccessRoute;
