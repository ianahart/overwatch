import { nanoid } from '@reduxjs/toolkit';
import { useCallback } from 'react';
import { debounce } from 'lodash';
import { AiFillStar, AiOutlineStar } from 'react-icons/ai';
import { IReviewFeedbackFormField } from '../../../../../interfaces';

export interface IReviewFeedbackRatingProps {
  field: IReviewFeedbackFormField;
  addRating: (name: string, rating: number) => void;
  isAlreadyReviewed: boolean;
}

const ReviewFeedbackRating = ({ field, addRating, isAlreadyReviewed }: IReviewFeedbackRatingProps) => {
  const NUM_OF_STARS = 5;

  const handleOnMouseEnter = useCallback(
    debounce((index: number) => {
      if (isAlreadyReviewed) {
        return;
      }
      addRating(field.name, index);
    }, 200),
    [addRating, field.name]
  );
  return (
    <div className="my-8">
      <div className="bg-gray-900 rounded p-2">
        <p className="text-sm">{field.desc}</p>
        <p>{field.title}:</p>
        <div className="flex items-center">
          {[...Array(NUM_OF_STARS)].map((_, index) => {
            index += 1;
            return (
              <div
                data-testid="feedback-star"
                className="cursor-pointer mx-1 text-lg"
                onMouseEnter={() => handleOnMouseEnter(index)}
                key={nanoid()}
              >
                {field.value >= index ? (
                  <AiFillStar data-testid="filled-star" className="text-yellow-400" />
                ) : (
                  <AiOutlineStar data-testid="outlined-star" className="text-gray-400" />
                )}
              </div>
            );
          })}
        </div>
      </div>
    </div>
  );
};

export default ReviewFeedbackRating;
