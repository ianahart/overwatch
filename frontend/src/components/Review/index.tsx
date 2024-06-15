import { useSelector } from 'react-redux';
import { useNavigate } from 'react-router-dom';
import { TRootState } from '../../state/store';

export interface IReviewsProps {
  userId: number;
  fullName: string;
  avatarUrl: string;
}

const Reviews = ({ userId, fullName, avatarUrl }: IReviewsProps) => {
  const { user } = useSelector((store: TRootState) => store.user);
  const navigate = useNavigate();

  const goToCreateReview = () => {
    navigate('/reviews/create', { state: { fullName, avatarUrl, reviewerId: userId, authorId: user.id } });
  };

  return (
    <div className="p-4 border-b border-gray-800">
      <h3 className="text-gray-400 text-lg">Reviews</h3>
      {user.role === 'USER' && (
        <div className="flex justify-end">
          <button onClick={goToCreateReview} className="text-sm font-bold text-green-400 underline cursor-pointer">
            Write review
          </button>
        </div>
      )}
    </div>
  );
};

export default Reviews;
