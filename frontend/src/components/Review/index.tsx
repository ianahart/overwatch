import { useSelector } from 'react-redux';
import { useNavigate } from 'react-router-dom';
import { useEffect, useState } from 'react';
import { TRootState, useLazyFetchReviewsQuery, useFetchReviewsQuery } from '../../state/store';
import { paginationState } from '../../data';
import { IPaginationState, IReview } from '../../interfaces';
import ReviewDisplay from './ReviewDisplay';
import Spinner from '../Shared/Spinner';

export interface IReviewsProps {
  userId: number;
  fullName: string;
  avatarUrl: string;
}

const Reviews = ({ userId, fullName, avatarUrl }: IReviewsProps) => {
  const { user, token } = useSelector((store: TRootState) => store.user);
  const [reviews, setReviews] = useState<IReview[]>([]);
  const [pag, setPag] = useState<IPaginationState>(paginationState);
  const [fetchReviews] = useLazyFetchReviewsQuery();

  const navigate = useNavigate();
  const { data, isLoading } = useFetchReviewsQuery(
    {
      userId,
      token,
      page: -1,
      pageSize: 2,
      direction: 'next',
    },
    { skip: !token || !userId }
  );

  useEffect(() => {
    if (data !== undefined) {
      const { items, page, pageSize, totalPages, direction, totalElements } = data.data;
      setPag((prevState) => ({
        ...prevState,
        page,
        pageSize,
        totalElements,
        totalPages,
        direction,
      }));
      setReviews(items);
    }
  }, [data]);

  const goToCreateReview = () => {
    navigate('/reviews/create', { state: { fullName, avatarUrl, reviewerId: userId, authorId: user.id } });
  };

  const paginateReviews = async (dir: string) => {
    try {
      const response = await fetchReviews({
        userId: userId,
        token,
        page: pag.page,
        pageSize: pag.pageSize,
        direction: dir,
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
      setReviews(items);
    } catch (err) {
      console.log(err);
    }
  };

  return (
    <div className="p-4 border-b border-gray-800">
      <h3 className="text-gray-400 text-lg">Reviews</h3>
      {isLoading && (
        <div className="flex justify-center my-4">
          <Spinner message="Loading reviews..." />
        </div>
      )}
      {user.role === 'USER' && (
        <div className="flex justify-end">
          <button onClick={goToCreateReview} className="text-sm font-bold text-green-400 underline cursor-pointer">
            Write review
          </button>
        </div>
      )}
      {reviews.map((review) => {
        return (
          <ReviewDisplay
            fullName={fullName}
            avatarUrl={avatarUrl}
            reviewerId={userId}
            key={review.id}
            review={review}
            currentUserId={user.id}
          />
        );
      })}

      <div className="flex items-center justify-center">
        {pag.page > 0 && (
          <button onClick={() => paginateReviews('prev')} className="mx-1">
            Prev
          </button>
        )}
        <p className="text-green-400">
          {pag.page + 1} of {pag.totalPages}
        </p>
        {pag.page < pag.totalPages - 1 && (
          <button onClick={() => paginateReviews('next')} className="mx-1">
            Next
          </button>
        )}
      </div>
    </div>
  );
};

export default Reviews;
