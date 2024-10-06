import { useLocation, useNavigate } from 'react-router-dom';
import { useEffect, useState } from 'react';
import { useSelector } from 'react-redux';

import ReviewFeedback from './ReviewFeedback';
import { IRepositoryReview, IReviewFeedbackForm } from '../../../../../interfaces';
import { reviewFeedbackState } from '../../../../../data';
import ReviewFeedbackRating from './ReviewFeedbackRating';
import { TRootState, useCreateReviewFeedbackMutation } from '../../../../../state/store';

const ReviewFeedbackRoute = () => {
  const { user, token } = useSelector((store: TRootState) => store.user);
  const location = useLocation();
  const navigate = useNavigate();
  const [error, setError] = useState('');
  const [reviewFeedbackForm, setReviewFeedbackForm] = useState<IReviewFeedbackForm>(reviewFeedbackState);
  const [createReviewFeedbackMut] = useCreateReviewFeedbackMutation();

  const { data }: { data: IRepositoryReview } = location.state || {};
  const { reviewerId, ownerId, id } = data;

  useEffect(() => {
    if (!reviewerId || !ownerId || !id) {
      navigate(-1);
    }
  }, [reviewerId, ownerId, id, navigate]);

  const addRating = (name: string, rating: number): void => {
    setReviewFeedbackForm((prevState) => ({
      ...prevState,
      [name]: { ...prevState[name as keyof IReviewFeedbackForm], value: rating },
    }));
  };

  const feedbackFilled = (): boolean => {
    return Object.values(reviewFeedbackForm).every((field) => field.value > 0);
  };

  const submitFeedback = (): void => {
    setError('');
    if (!feedbackFilled()) {
      setError('Please make sure to fill out all ratings');
      return;
    }

    const payload = {
      token,
      reviewerId,
      ownerId,
      repositoryId: id,
      clarity: reviewFeedbackForm.clarity.value,
      helpfulness: reviewFeedbackForm.helpfulness.value,
      responseTime: reviewFeedbackForm.responseTime.value,
      thoroughness: reviewFeedbackForm.thoroughness.value,
    };

    createReviewFeedbackMut(payload)
      .unwrap()
      .then((res) => {
        console.log(res);
        navigate(`/dashboard/${user.slug}/user/reviews`);
      })
      .catch((err) => {
        console.log(err);
      });
  };

  return (
    <div>
      <div className="my-4">
        <h3>
          Feedback from {data.firstName} {data.lastName} on <span className="font-bold">{data.repoName}</span>
        </h3>
      </div>
      <div className="my-4">
        <ReviewFeedback feedback={data.feedback} />
        <div className="my-4">
          <h3>Please take a few moments and give your feedback about the review.</h3>
        </div>
        {error.length > 0 && (
          <div className="my-2">
            <p className="text-sm text-red-300">{error}</p>
          </div>
        )}
        <ReviewFeedbackRating field={reviewFeedbackForm.clarity} addRating={addRating} />
        <ReviewFeedbackRating field={reviewFeedbackForm.helpfulness} addRating={addRating} />
        <ReviewFeedbackRating field={reviewFeedbackForm.thoroughness} addRating={addRating} />
        <ReviewFeedbackRating field={reviewFeedbackForm.responseTime} addRating={addRating} />
      </div>
      <div className="flex justify-center">
        <button onClick={submitFeedback} className="btn">
          Submit Feedback
        </button>
      </div>
    </div>
  );
};

export default ReviewFeedbackRoute;
