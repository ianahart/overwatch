import { useEffect } from 'react';
import { useSearchParams, useNavigate } from 'react-router-dom';
import { TRootState, useLazyFetchGitHubAccessTokenQuery } from '../state/store';
import { useSelector } from 'react-redux';
import { retrieveTokens } from '../util';
import { Role } from '../enums';
import { Session } from '../util/SessionService';

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
            const { githubId } = response;
            console.log(githubId);
            if (user.role === Role.REVIEWER) {
              Session.setItem(githubId.toString());
              navigate(`/dashboard/${user.slug}/reviewer/reviews`);
              //navigate(-1);
            } else {
              navigate(`/dashboard/${user.slug}/user/add-review?verified=true`, { state: { githubId } });
            }
          } catch (err) {
            console.log(err);
          }
        };
        makeReq();
      }
    }
  }, [searchParams, token, user.slug, user.role, navigate]);

  return <div></div>;
};

export default GitHubSuccessRoute;
