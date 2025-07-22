import { useSelector, useDispatch } from 'react-redux';
import {
  TRootState,
  setRepositoryPagination,
  setRepositoryReviews,
  useLazyFetchRepositoriesQuery,
} from '../../../../../state/store';
import RepositoryReviewListItem from './RepositoryReviewListItem';
import { retrieveTokens } from '../../../../../util';

const RepositoryReviewList = () => {
  const dispatch = useDispatch();
  const { repositoryReviews, pagination, sortFilter, statusFilter, languageFilter } = useSelector(
    (store: TRootState) => store.repositoryReviews
  );
  const [fetchRepositories] = useLazyFetchRepositoriesQuery();

  const handlePagination = (direction: string) => {
    const repositoryPayload = {
      token: retrieveTokens().token,
      page: pagination.page,
      pageSize: pagination.pageSize,
      direction: direction,
      sortFilter,
      statusFilter,
      languageFilter,
    };
    fetchRepositories(repositoryPayload)
      .unwrap()
      .then((res) => {
        const { direction, items, page, pageSize, totalElements, totalPages } = res.data;
        const pagination = { direction, page, pageSize, totalElements, totalPages };
        dispatch(setRepositoryReviews(items));
        dispatch(setRepositoryPagination(pagination));
      })
      .catch((err) => {
        console.log(err);
      });
  };

  return (
    <div data-testid="RepositoryReviewList" className="mt-12 mb-12">
      <ul>
        {repositoryReviews.map((repositoryReview) => {
          repositoryReview;
          return <RepositoryReviewListItem key={repositoryReview.id} data={repositoryReview} />;
        })}
      </ul>
      <div className="flex items-center text-gray-400 justify-center">
        {pagination.page > 0 && (
          <button onClick={() => handlePagination('prev')} className="mx-2">
            Prev
          </button>
        )}
        <p className="mx-2">{pagination.page + 1}</p>
        {pagination.page < pagination.totalPages - 1 && (
          <button onClick={() => handlePagination('next')} className="mx-2">
            Next
          </button>
        )}
      </div>
    </div>
  );
};

export default RepositoryReviewList;
