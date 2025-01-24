import { useCallback, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { useParams } from 'react-router-dom';
import { BsSearch } from 'react-icons/bs';
import { nanoid } from 'nanoid';

import { languageMap } from '../../../../../util';
import {
  TRootState,
  setInitialRepositoryTree,
  setRepository,
  setRepositoryFile,
  setRepositoryNavView,
  setRepositoryPage,
  setRepositoryTree,
  setSearchingCodeQuery,
  useCreateRepositoryFileMutation,
  useLazyFetchRepositoryQuery,
  useLazySearchRepositoryQuery,
} from '../../../../../state/store';
import { Session } from '../../../../../util/SessionService';
import { IGitHubTree } from '../../../../../interfaces';
import { ERepositoryView } from '../../../../../enums';

const FileTree = () => {
  const params = useParams();
  const dispatch = useDispatch();
  const repositoryId = Number.parseInt(params.id as string);
  let githubId = Session.getItem('github_access_token') ?? '';
  console.log(githubId);
  const { token } = useSelector((store: TRootState) => store.user);
  const { repositoryTree, searchingCodeQuery, repositoryNavView, repositoryPage, repository } = useSelector(
    (store: TRootState) => store.repositoryTree
  );
  const [fetchRepository, { isLoading }] = useLazyFetchRepositoryQuery();
  const [searchRepository] = useLazySearchRepositoryQuery();
  const [createRepositoryFile] = useCreateRepositoryFileMutation();
  const [searchText, setSearchText] = useState('');

  const handleOnLoadMoreFiles = useCallback(
    (reset: boolean = false) => {
      if (isLoading) return;
      const page = reset ? 0 : repositoryPage;

      if (reset) {
        dispatch(setRepositoryPage(0));
        dispatch(setSearchingCodeQuery(''));
        dispatch(setInitialRepositoryTree([]));
      }
      const parsedGithubId = Number.parseInt(githubId);

      fetchRepository({ repositoryId, token, githubId: parsedGithubId, repositoryPage: page })
        .unwrap()
        .then((res) => {
          dispatch(setRepository(res.data.repository));
          if (reset) {
            dispatch(setInitialRepositoryTree(res.data.contents.tree));
          } else {
            dispatch(setRepositoryTree(res.data.contents.tree));
          }
          dispatch(setRepositoryPage(repositoryPage + 1));
        })
        .catch((err) => {
          console.log(err);
        });
    },
    [dispatch, fetchRepository, repositoryId, token, githubId, repositoryPage, isLoading]
  );

  const handleOnLoadMoreSearchFiles = useCallback(
    (reset: boolean = false) => {
      const page = reset ? 0 : repositoryPage;

      if (reset) {
        dispatch(setRepositoryPage(0));
        dispatch(setInitialRepositoryTree([]));
      }

      const parsedGithubId = Number.parseInt(githubId);

      const payload = {
        token,
        githubId: parsedGithubId,
        repositoryPage: page,
        repoName: repository.repoName,
        query: searchingCodeQuery,
      };
      searchRepository(payload)
        .unwrap()
        .then((res) => {
          if (reset) {
            console.log('RUN');
            dispatch(setInitialRepositoryTree(res.data.contents.tree));
          } else {
            dispatch(setRepositoryTree(res.data.contents.tree));
          }
          dispatch(setRepositoryPage(repositoryPage + 1));
          console.log(res);
        })
        .catch((err) => {
          console.log(err);
        });
    },
    [dispatch, searchRepository, token, githubId, repositoryPage, repository.repoName, searchingCodeQuery]
  );

  const scrollToTop = useCallback(() => {
    window.scrollTo({ top: 0, behavior: 'smooth' });
  }, []);

  const handleOnClickFile = useCallback(
    (path: string) => {
      const [owner, repoName] = repository.repoName.split('/');
      const parsedGithubId = Number.parseInt(githubId);
      createRepositoryFile({ owner, repoName, path, token, githubId: parsedGithubId })
        .unwrap()
        .then((res) => {
          const content = atob(res.data);
          let extension = path.split('.').pop();
          let language = '';
          if (extension !== undefined) {
            language = languageMap[extension] || 'text';
          } else {
            language = 'text';
          }
          if (repositoryNavView === ERepositoryView.DETAILS) {
            dispatch(setRepositoryNavView(ERepositoryView.CODE));
          }

          dispatch(setRepositoryFile({ path, content, language }));
          scrollToTop();
        })
        .catch((err) => {
          console.log(err);
        });
    },
    [createRepositoryFile, repository.repoName, token, githubId, repositoryNavView, dispatch, scrollToTop]
  );

  const filteredRepositoryTree: IGitHubTree[] = repositoryTree.filter((node) =>
    node.path.toLowerCase().includes(searchText.toLowerCase())
  );

  return (
    <div className="md:w-[300px] min-h-[90vh] p-2">
      <div>
        <div className="w-full">
          <div className="relative">
            <input
              value={searchText}
              onChange={(e) => setSearchText(e.target.value)}
              placeholder="Search files..."
              className="placeholder:p-8 w-full h-9 rounded-lg bg-gray-950 text-gray-400 px-8"
            />
            <BsSearch className="absolute top-2 left-2 text-lg" />
            <p className="text-sm my-2 text-green-400 font-bold">
              <span className="text-yellow-400">*</span> Highlight code with your mouse or click to narrow down code
              files.
            </p>
            <button onClick={() => handleOnLoadMoreFiles(true)} className="my-2 p-1 rounded border border-gray-800">
              Reset file tree
            </button>
          </div>
        </div>
        <ul className="overflow-auto">
          {filteredRepositoryTree.map((node) => {
            return (
              <li
                onClick={() => handleOnClickFile(node.path)}
                key={nanoid()}
                className="text-sm cursor-pointer p-2 border-b border-gray-800 text-gray-400 my-2 hover:bg-gray-700"
              >
                {node.path}
              </li>
            );
          })}
        </ul>
        <div className="my-4 flex justify-center">
          <button
            onClick={() =>
              searchingCodeQuery.length > 0 ? handleOnLoadMoreSearchFiles(false) : handleOnLoadMoreFiles(false)
            }
          >
            More files...
          </button>
        </div>
      </div>
    </div>
  );
};

export default FileTree;
