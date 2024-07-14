import { useEffect, useRef, useState } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { useLocation, useSearchParams } from 'react-router-dom';

import { Session } from '../../../../../util/SessionService';
import Reviewer from './Reviewer';
import { paginationState } from '../../../../../data';
import {
  TRootState,
  clearAddReview,
  setReviewers,
  setSelectedReviewer,
  useFetchConnectionsQuery,
  useLazyFetchConnectionsQuery,
} from '../../../../../state/store';
import { retrieveTokens } from '../../../../../util';
import Spinner from '../../../../Shared/Spinner';
import ChosenReviewer from './ChosenReviewer';
import GitHubLogin from './GitHubLogin';
import RepositoryList from './RepositoryList';

const AddReview = () => {
  const [searchParams, _] = useSearchParams();
  const location = useLocation();
  const dispatch = useDispatch();
  const shouldRun = useRef(true);
  const [pag, setPag] = useState(paginationState);
  const [fetchConnections, { isLoading: fetchLoading }] = useLazyFetchConnectionsQuery();

  const { user } = useSelector((store: TRootState) => store.user);
  const { reviewers, selectedReviewer } = useSelector((store: TRootState) => store.addReview);

  const { data, isLoading } = useFetchConnectionsQuery({
    userId: user.id,
    token: retrieveTokens()?.token,
    page: -1,
    pageSize: 3,
    direction: 'next',
    override: 'true',
  });

  useEffect(() => {
    if (!Session.getItem('github_access_token') && searchParams.has('verified')) {
      Session.setItem(location.state.accessToken);
    }
  }, [location, searchParams]);

  useEffect(() => {
    return () => {
      dispatch(clearAddReview());
    };
  }, [dispatch]);

  useEffect(() => {
    if (data !== undefined && shouldRun.current) {
      shouldRun.current = false;
      const { items, page, pageSize, totalPages, direction, totalElements } = data.data;
      setPag((prevState) => ({
        ...prevState,
        page,
        pageSize,
        totalElements,
        totalPages,
        direction,
      }));
      dispatch(setReviewers(items));
    }
  }, [data, dispatch, shouldRun.current]);

  const paginateConnections = async (dir: string) => {
    try {
      if (fetchLoading) return;

      const response = await fetchConnections({
        userId: user.id,
        token: retrieveTokens().token,
        page: pag.page,
        pageSize: pag.pageSize,
        direction: dir,
        override: 'true',
      }).unwrap();

      const { items, page, pageSize, totalPages, direction, totalElements } = response.data;
      setPag((prevState) => ({
        ...prevState,
        page,
        pageSize,
        totalElements,
        totalPages,
        direction,
      }));
      dispatch(setReviewers(items));
    } catch (err) {
      console.log(err);
    }
  };

  useEffect(() => {
    const storedSelectedReviewer = localStorage.getItem('selected_reviewer');
    if (selectedReviewer.id === 0 && storedSelectedReviewer && reviewers.length) {
      const reviewer = [...reviewers].find(
        (reviewer) => reviewer.receiverId === Number.parseInt(storedSelectedReviewer)
      );
      if (reviewer) {
        dispatch(setSelectedReviewer({ reviewer }));
      }
    }
  }, [selectedReviewer.id, reviewers]);

  return (
    <div>
      <div className="my-8 flex justify-center">
        <ChosenReviewer />
      </div>
      <div className="user-dashboard-add-review my-8">
        {Session.getItem('github_access_token') && (
          <div>
            {isLoading && <Spinner message="Loading reviewers..." />}
            <div className="my-2 md:w-[250px] w-full">
              <p className="text-gray-400">Select a connection that you want to review your code.</p>
            </div>
            {reviewers.length > 0 && (
              <ul>
                {reviewers.map((reviewer) => {
                  return <Reviewer key={reviewer.id} data={reviewer} />;
                })}
              </ul>
            )}
            {pag.page < pag.totalPages - 1 && (
              <div className="my-4">
                <button
                  onClick={() => paginateConnections('next')}
                  className="!text-gray-400 outline-btn border border-gray-800 rounded"
                >
                  Load more...
                </button>
              </div>
            )}
          </div>
        )}
        <div className="md:w-[40%] w-full p-4">
          {Session.getItem('github_access_token') && localStorage.getItem('selected_reviewer') && <RepositoryList />}
        </div>
        <div className="min-w-[200px]">{!Session.getItem('github_access_token') && <GitHubLogin />}</div>
      </div>
    </div>
  );
};

export default AddReview;
