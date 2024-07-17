import { useParams } from 'react-router-dom';
import { retrieveTokens } from '../../../../../util';
import {
  TRootState,
  clearRepositoryTree,
  setRepository,
  setRepositoryPage,
  setRepositoryTree,
  useLazyFetchRepositoryQuery,
} from '../../../../../state/store';
import { useEffect, useRef } from 'react';
import { Session } from '../../../../../util/SessionService';
import { useDispatch, useSelector } from 'react-redux';
import FileTree from './FileTree';
import CodeViewer from './CodeViewer';

const RepositoryReview = () => {
  const dispatch = useDispatch();
  const shouldRun = useRef(true);
  const { repositoryPage } = useSelector((store: TRootState) => store.repositoryTree);
  const params = useParams();

  const token = retrieveTokens()?.token;
  const accessToken = Session.getItem('github_access_token');
  const repositoryId = Number.parseInt(params.id as string);

  const [fetchRepository] = useLazyFetchRepositoryQuery();

  useEffect(() => {
    return () => {
      dispatch(clearRepositoryTree());
    };
  }, [dispatch]);

  useEffect(() => {
    if (accessToken === null) {
      const gitHubClientId = import.meta.env.VITE_GITHUB_CLIENT_ID;
      const githubLoginUrl = `https://github.com/login/oauth/authorize?client_id=${gitHubClientId}`;
      window.location.assign(githubLoginUrl);
    }
    if (accessToken !== null && shouldRun.current) {
      shouldRun.current = false;
      fetchRepository({ repositoryId, token, accessToken, repositoryPage })
        .unwrap()
        .then((res) => {
          dispatch(setRepository(res.data.repository));
          dispatch(setRepositoryTree(res.data.tree));
          dispatch(setRepositoryPage(1));
        })
        .catch((err) => {
          console.log(err);
        });
    }
  }, [accessToken, shouldRun.current, fetchRepository, repositoryId, token, repositoryPage]);

  return (
    <div className="md:max-w-[1450px] w-full mx-auto mt-8">
      <div className="md:flex bg-gray-900 p-2 rounded">
        <FileTree />
        <CodeViewer />
      </div>
    </div>
  );
};

export default RepositoryReview;
