import { useEffect } from 'react';
import { useDispatch } from 'react-redux';

import ReviewsFilters from './ReviewsFilters';
import { clearRepositoryReviews } from '../../../../../state/store';
import RepositoryReviewList from './RepositoryReviewList';

const Reviews = () => {
  const dispatch = useDispatch();

  useEffect(() => {
    return () => {
      dispatch(clearRepositoryReviews());
    };
  }, [dispatch]);

  return (
    <div>
      <ReviewsFilters />
      <RepositoryReviewList />
    </div>
  );
};

export default Reviews;
