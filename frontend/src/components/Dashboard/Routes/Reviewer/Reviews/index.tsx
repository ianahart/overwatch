import { useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { useNavigate } from 'react-router-dom';

import { TRootState, clearRepositoryReviews, useFetchPaymentMethodQuery } from '../../../../../state/store';
import RepositoryReviewList from '../../User/Reviews/RepositoryReviewList';
import ReviewsFilters from '../../User/Reviews/ReviewsFilters';

const Reviews = () => {
  const { token, user } = useSelector((store: TRootState) => store.user);
  const dispatch = useDispatch();
  const navigate = useNavigate();

  useEffect(() => {
    return () => {
      dispatch(clearRepositoryReviews());
    };
  }, [dispatch]);

  const { data, error } = useFetchPaymentMethodQuery({ token, userId: user.id });

  useEffect(() => {
    if (error) {
      navigate(`/settings/${user.slug}/billing?toast=show`);
    }
  }, [data, navigate, error]);

  return (
    <div>
      <ReviewsFilters />
      <RepositoryReviewList />
    </div>
  );
};

export default Reviews;
