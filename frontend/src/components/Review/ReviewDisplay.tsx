import { AiFillStar } from 'react-icons/ai';
import { IReview } from '../../interfaces';
import Avatar from '../Shared/Avatar';
import dayjs from 'dayjs';

export interface IReviewDisplayProps {
  review: IReview;
}

const ReviewDisplay = ({ review }: IReviewDisplayProps) => {
  console.log(review);
  return (
    <div className="my-4 p-4">
      <div className="flex items-center">
        <Avatar width="w-10" height="h-10" initials="?.?" avatarUrl={review.avatarUrl} />
        <div>
          <p className=" ml-2 text-gray-400">{review.name}</p>
          {review.isEdited && <p className="ml-2 text-xs">(edited)</p>}
          <p className="text-xs">
            <span className="italic mx-1">posted on</span>
            {dayjs(review.createdAt).format('MM/DD/YYYY')}
          </p>
        </div>
      </div>
      <div className="flex justify-end">
        <div className="flex items-center">
          <AiFillStar className="text-lg text-yellow-400" />
          <p className="text-gray-400">{review.rating}</p>
        </div>
      </div>
      <div className="my-2">
        <p>{review.review}</p>
      </div>
    </div>
  );
};

export default ReviewDisplay;
