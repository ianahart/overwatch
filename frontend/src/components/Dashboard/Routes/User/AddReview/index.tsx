import { useEffect, useRef, useState } from 'react';
import { useSelector, useDispatch } from 'react-redux';

import Reviewer from './Reviewer';
import { paginationState } from '../../../../../data';
import {
  TRootState,
  clearAddReview,
  setReviewers,
  useFetchConnectionsQuery,
  useLazyFetchConnectionsQuery,
} from '../../../../../state/store';
import { retrieveTokens } from '../../../../../util';
import Spinner from '../../../../Shared/Spinner';
import ChosenReviewer from './ChosenReviewer';

const AddReview = () => {
  const dispatch = useDispatch();
  const shouldRun = useRef(true);
  const [pag, setPag] = useState(paginationState);
  const [fetchConnections, { isLoading: fetchLoading }] = useLazyFetchConnectionsQuery();

  const { user } = useSelector((store: TRootState) => store.user);
  const { reviewers } = useSelector((store: TRootState) => store.addReview);

  const { data, isLoading } = useFetchConnectionsQuery({
    userId: user.id,
    token: retrieveTokens()?.token,
    page: -1,
    pageSize: 3,
    direction: 'next',
    override: 'true',
  });

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

  return (
    <div>
      <div className="my-8 flex justify-center">
        <ChosenReviewer />
      </div>
      <div className="flex justify-between my-8">
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

        <div>{/*GITHUB STUFF GOES HERE*/}</div>
      </div>
    </div>
  );
};

export default AddReview;
