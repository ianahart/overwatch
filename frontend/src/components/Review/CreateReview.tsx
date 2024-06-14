import { nanoid } from '@reduxjs/toolkit';
import { useState } from 'react';
import { AiFillStar, AiOutlineStar } from 'react-icons/ai';
import { useLocation } from 'react-router-dom';
import Avatar from '../Shared/Avatar';

const CreateReview = () => {
  const NUM_OF_STARS = 5;
  const { authorId, reviewerId, avatarUrl, fullName } = useLocation().state;
  const [error, setError] = useState('');
  const [rating, setRating] = useState(0);
  const [review, setReview] = useState('');

  const handleOnMouseEnter = (index: number) => {
    setRating(index);
  };

  const handleOnSubmit = (e: React.ChangeEvent<HTMLFormElement>) => {
    e.preventDefault();
    setError('');
    if (rating === 0 || review.trim().length === 0 || review.length > 400) {
      setError('Please make sure you fill out all fields and the review does not exceed 400 characters');
    }
  };

  return (
    <div>
      <div className="flex justify-center mt-40">
        <div className="max-w-[600px] w-full border rounded-lg border-gray-800 p-4">
          <form onSubmit={handleOnSubmit}>
            <div className="flex justify-center my-8">
              <h2 className="text-2xl text-gray-400">Write a review</h2>
            </div>
            <div className="my-2 flex items-center flex-col">
              <p>Reviewing {fullName}</p>
              <div className="my-1">
                <Avatar height="h-10" width="w-10" initials="?.?" avatarUrl={avatarUrl} />
              </div>
            </div>
            <div>
              <p className="text-gray-400">Rating</p>
              <div className="my-2">
                <p className="text-xs">Did you enjoy this reviewer's work?</p>
                <p className="text-xs">Rate the reviewer by coloring in the stars.</p>
              </div>
              <div className="flex items-center">
                {[...Array(NUM_OF_STARS)].map((_, index) => {
                  index += 1;
                  return (
                    <div
                      className="cursor-pointer mx-1 text-lg"
                      onMouseEnter={() => handleOnMouseEnter(index)}
                      key={nanoid()}
                    >
                      {rating >= index ? (
                        <AiFillStar className="text-yellow-400" />
                      ) : (
                        <AiOutlineStar className="text-gray-400" />
                      )}
                    </div>
                  );
                })}
              </div>
              <div className="my-8">
                <label className="text-gray-400">Review</label>
                <div className="my-2">
                  <p className="text-xs">What were they most helpful with?</p>
                </div>
                <textarea
                  value={review}
                  onChange={(e) => setReview(e.target.value)}
                  className="w-full bg-transparent rounded-lg border border-gray-800 h-24 resize-none"
                ></textarea>
              </div>
              {error.length > 0 && (
                <div className="my-2 text-center">
                  <p className="text-red-300 text-xs">{error}</p>
                </div>
              )}
              <div className="my-8">
                <button type="submit" className="btn w-full">
                  Submit
                </button>
              </div>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
};

export default CreateReview;
