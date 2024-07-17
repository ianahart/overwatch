import { useDispatch, useSelector } from 'react-redux';
import { useParams } from 'react-router-dom';
import { nanoid } from 'nanoid';

import { languageMap } from '../../../../../util';
import {
  TRootState,
  setRepository,
  setRepositoryFile,
  setRepositoryPage,
  setRepositoryTree,
  useCreateRepositoryFileMutation,
  useLazyFetchRepositoryQuery,
} from '../../../../../state/store';
import { Session } from '../../../../../util/SessionService';

const FileTree = () => {
  const params = useParams();
  const dispatch = useDispatch();
  const repositoryId = Number.parseInt(params.id as string);
  const accessToken = Session.getItem('github_access_token') ?? '';
  const { token } = useSelector((store: TRootState) => store.user);
  const { repositoryTree, repositoryPage, repository } = useSelector((store: TRootState) => store.repositoryTree);
  const [fetchRepository, { isLoading }] = useLazyFetchRepositoryQuery();
  const [createRepositoryFile] = useCreateRepositoryFileMutation();

  const handleOnLoadMoreFiles = () => {
    if (isLoading) return;
    fetchRepository({ repositoryId, token, accessToken, repositoryPage })
      .unwrap()
      .then((res) => {
        dispatch(setRepository(res.data.repository));
        dispatch(setRepositoryTree(res.data.tree));
        dispatch(setRepositoryPage(repositoryPage + 1));
      })
      .catch((err) => {
        console.log(err);
      });
  };
  const handleOnClickFile = (path: string) => {
    const [owner, repoName] = repository.repoName.split('/');
    createRepositoryFile({ owner, repoName, path, token, accessToken })
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

        dispatch(setRepositoryFile({ path, content, language }));
      })
      .catch((err) => {
        console.log(err);
      });
  };

  return (
    <div className="md:w-[300px] min-h-[90vh] p-2">
      <div className="overflow-scroll overflow-x-auto">
        <ul>
          {repositoryTree.map((node) => {
            return node.type === 'blob' ? (
              <li
                onClick={() => handleOnClickFile(node.path)}
                key={nanoid()}
                className="text-sm cursor-pointer p-2 border-b border-gray-700 text-gray-400 my-2 hover:bg-gray-700"
              >
                {node.path}
              </li>
            ) : null;
          })}
        </ul>
        <div className="my-4 flex justify-center">
          <button onClick={handleOnLoadMoreFiles}>More files...</button>
        </div>
      </div>
    </div>
  );
};

export default FileTree;
