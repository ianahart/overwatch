import { useEffect } from 'react';
import { useDispatch } from 'react-redux';

import { clearRepositoryReviews } from '../../../../../state/store';
import RepositoryReviewList from '../../User/Reviews/RepositoryReviewList';
import ReviewsFilters from '../../User/Reviews/ReviewsFilters';

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
