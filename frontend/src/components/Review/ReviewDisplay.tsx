import { AiFillEdit, AiFillStar } from 'react-icons/ai';
import dayjs from 'dayjs';
import { BsThreeDots, BsTrash } from 'react-icons/bs';
import { useEffect, useRef, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useSelector } from 'react-redux';

import { IReview } from '../../interfaces';
import Avatar from '../Shared/Avatar';
import { TRootState, useDeleteReviewMutation } from '../../state/store';

export interface IReviewDisplayProps {
  review: IReview;
  currentUserId: number;
  avatarUrl: string;
  reviewerId: number;
  fullName: string;
}

const ReviewDisplay = ({ review, currentUserId, reviewerId, avatarUrl, fullName }: IReviewDisplayProps) => {
  const navigate = useNavigate();
  const { token } = useSelector((store: TRootState) => store.user);
  const [deleteReview] = useDeleteReviewMutation();
  const [menuOpen, setMenuOpen] = useState(false);
  const menuRef = useRef<HTMLDivElement>(null);
  const triggerRef = useRef<HTMLDivElement>(null);

  const clickAway = (e: MouseEvent) => {
    const target = e.target as Element;

    if (menuRef.current !== null && triggerRef.current !== null) {
      if (!menuRef.current.contains(target) && !triggerRef.current.contains(target)) {
        setMenuOpen(false);
      }
    }
  };

  useEffect(() => {
    window.addEventListener('click', clickAway);
    return () => window.removeEventListener('click', clickAway);
  }, [clickAway]);

  const handleEdit = () => {
    setMenuOpen(false);
    navigate(`/reviews/${review.id}/edit`, { state: { fullName, avatarUrl, reviewerId, authorId: review.authorId } });
  };

  const handleDelete = () => {
    setMenuOpen(false);
    deleteReview({ token, reviewId: review.id });
  };

  return (
    <div data-testid="review-display-item" className="my-4 p-4">
      {currentUserId === review.authorId && (
        <div className="flex justify-end">
          <div className="relative">
            <div data-testid="review-display-menu-container" ref={triggerRef} onClick={() => setMenuOpen(true)}>
              <BsThreeDots />
            </div>
            {menuOpen && (
              <div ref={menuRef} className="bg-stone-900 absolute top-4 right-0 rounded shadow-md p-2 w-28">
                <div
                  data-testid="review-display-menu-edit"
                  className="flex items-center my-1 cursor-pointer"
                  onClick={handleEdit}
                >
                  <AiFillEdit className="mr-1" />
                  <p className="text-sm">Edit</p>
                </div>
                <div
                  data-testid="review-display-menu-delete"
                  className="flex items-center my-1 cursor-pointer"
                  onClick={handleDelete}
                >
                  <BsTrash className="mr-1" />
                  <p className="text-sm">Delete</p>
                </div>
              </div>
            )}
          </div>
        </div>
      )}
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
