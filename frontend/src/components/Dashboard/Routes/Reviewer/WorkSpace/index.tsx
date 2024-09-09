import { Outlet, useNavigate } from 'react-router-dom';
import { useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';

import WorkSpaceTitle from './WorkspaceTitle';
import {
  TRootState,
  clearWorkSpace,
  setTodoLists,
  setWorkSpace,
  useFetchLatestWorkspaceQuery,
  useLazyFetchTodoListsQuery,
} from '../../../../../state/store';
import CurrentWorkSpaces from './CurrentWorkSpaces';
import { retrieveTokens } from '../../../../../util';
import Spinner from '../../../../Shared/Spinner';

const WorkSpaceContainer = () => {
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const token = retrieveTokens()?.token;
  const { workSpace } = useSelector((store: TRootState) => store.workSpace);
  const { user } = useSelector((store: TRootState) => store.user);
  const { data, isLoading } = useFetchLatestWorkspaceQuery({ token, userId: user.id });
  const [fetchTodoLists] = useLazyFetchTodoListsQuery();

  useEffect(() => {
    if (data !== undefined) {
      if (data.data !== null) {
        dispatch(setWorkSpace(data.data));
      }
    }
  }, [data, dispatch]);

  useEffect(() => {
    if (workSpace.id !== 0) {
      fetchTodoLists({ token, workSpaceId: workSpace.id })
        .unwrap()
        .then((res) => {
          dispatch(setTodoLists(res.data));
          navigate(`/dashboard/${user.slug}/reviewer/workspaces/${workSpace.id}`);
        })
        .catch((err) => {
          console.log(err);
        });
    }
  }, [workSpace.id]);

  useEffect(() => {
    return () => {
      dispatch(clearWorkSpace());
    };
  }, []);

  return (
    <div className="max-w-full overflow-hidden">
      <CurrentWorkSpaces />
      <WorkSpaceTitle />
      <div className="min-h-[700px]">
        {isLoading && (
          <div className="flex justify-center">
            <Spinner message="Loading..." />
          </div>
        )}
        {!isLoading && <Outlet />}
      </div>
    </div>
  );
};

export default WorkSpaceContainer;
