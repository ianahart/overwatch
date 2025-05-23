import { useParams } from 'react-router-dom';
import { BiBookContent } from 'react-icons/bi';
import { FaCode } from 'react-icons/fa';
import { useDispatch, useSelector } from 'react-redux';
import { useEffect, useRef } from 'react';

import { retrieveTokens } from '../../../../../util';
import {
  TRootState,
  clearRepositoryTree,
  setRepository,
  setRepositoryLanguages,
  setRepositoryPage,
  setRepositoryTree,
  useLazyFetchRepositoryQuery,
} from '../../../../../state/store';
import { Session } from '../../../../../util/SessionService';
import FileTree from './FileTree';
import CodeViewer from './CodeViewer';
import NavigationButton from './NavigationButton';
import { ERepositoryView } from '../../../../../enums';
import RepositoryDetails from './RepositoryDetails';

const RepositoryReview = () => {
  const dispatch = useDispatch();
  const shouldRun = useRef(true);
  const { repositoryPage, repositoryNavView, repository } = useSelector((store: TRootState) => store.repositoryTree);
  const params = useParams();

  const token = retrieveTokens()?.token;
  const githubId = Session.getItem('github_access_token') ?? '';
  const repositoryId = Number.parseInt(params.id as string);

  const [fetchRepository] = useLazyFetchRepositoryQuery();

  useEffect(() => {
    return () => {
      dispatch(clearRepositoryTree());
    };
  }, [dispatch]);

  useEffect(() => {
    if (githubId === '' || githubId === null) {
      console.log('run');
      const gitHubClientId = import.meta.env.VITE_GITHUB_CLIENT_ID;
      const githubLoginUrl = `https://github.com/login/oauth/authorize?client_id=${gitHubClientId}`;
      window.location.assign(githubLoginUrl);
    }
    if (githubId !== null && shouldRun.current) {
      shouldRun.current = false;
      fetchRepository({ repositoryId, token, githubId: Number.parseInt(githubId), repositoryPage })
        .unwrap()
        .then((res) => {
          localStorage.setItem('content', res.data.repository.feedback);
          dispatch(setRepository(res.data.repository));
          dispatch(setRepositoryTree(res.data.contents.tree));
          dispatch(setRepositoryLanguages(res.data.contents.languages));
          dispatch(setRepositoryPage(1));
        })
        .catch((err) => {
          console.log(err);
        });
    }
  }, [githubId, shouldRun.current, fetchRepository, repositoryId, token, repositoryPage]);

  useEffect(() => {
    return () => {
      localStorage.removeItem('content');
    };
  }, []);

  return (
    <div className="md:max-w-[1450px] w-full mx-auto mt-8">
      <div className="bg-gray-900 p-2 rounded">
        <div className="my-4 flex">
          <NavigationButton keyword={ERepositoryView.DETAILS} text="Details" icon={<BiBookContent />} />
          <NavigationButton keyword={ERepositoryView.CODE} text="Code" icon={<FaCode />} />
        </div>
        <div className="md:flex">
          <FileTree />
          {repository.id !== 0 && repositoryNavView === ERepositoryView.CODE && <CodeViewer />}
          {repository.id !== 0 && repositoryNavView === ERepositoryView.DETAILS && <RepositoryDetails />}
        </div>
      </div>
    </div>
  );
};

export default RepositoryReview;
