import { useLocation, useNavigate } from 'react-router-dom';
import { useEffect, useState } from 'react';
import { useSelector } from 'react-redux';

import ReviewFeedback from './ReviewFeedback';
import { IError, IRepositoryReview, IReviewFeedbackForm } from '../../../../../interfaces';
import { reviewFeedbackState } from '../../../../../data';
import ReviewFeedbackRating from './ReviewFeedbackRating';
import {
  TRootState,
  useCreateReviewFeedbackMutation,
  useLazyGetSingleReviewFeedbackQuery,
} from '../../../../../state/store';
import { nanoid } from 'nanoid';

const ReviewFeedbackRoute = () => {
  const { user, token } = useSelector((store: TRootState) => store.user);
  const location = useLocation();
  const navigate = useNavigate();
  const [errors, setErrors] = useState<string[]>([]);
  const [reviewFeedbackForm, setReviewFeedbackForm] = useState<IReviewFeedbackForm>(reviewFeedbackState);
  const [createReviewFeedbackMut] = useCreateReviewFeedbackMutation();
  const [getSingleReviewFeedback] = useLazyGetSingleReviewFeedbackQuery();
  const [isAlreadyReviewed, setIsAlreadyReviewed] = useState(false);

  const { data }: { data: IRepositoryReview } = location.state || {};
  const { reviewerId, ownerId, id } = data;

  useEffect(() => {
    if (!reviewerId || !ownerId || !id) {
      navigate(-1);
    } else {
      getSingleReviewFeedback({ reviewerId, ownerId, repositoryId: id, token })
        .unwrap()
        .then((res) => {
          if (res.data !== null) {
            const { data: reviewFeedbackData } = res;
            for (let prop in reviewFeedbackData) {
              if (Object.keys(reviewFeedbackForm).includes(prop)) {
                const rating = reviewFeedbackData[prop] as number;
                addRating(prop, rating);
              }
            }
            setIsAlreadyReviewed(true);
          }
        })
        .catch((err) => {
          console.log(err);
        });
    }
  }, [reviewerId, ownerId, id, navigate, token]);

  const addRating = (name: string, rating: number): void => {
    setReviewFeedbackForm((prevState) => ({
      ...prevState,
      [name]: { ...prevState[name as keyof IReviewFeedbackForm], value: rating },
    }));
  };

  const feedbackFilled = (): boolean => {
    return Object.values(reviewFeedbackForm).every((field) => field.value > 0);
  };

  const applyServerErrors = <T extends IError>(data: T): void => {
    for (let prop in data) {
      setErrors((prevState) => [...prevState, data[prop]]);
    }
  };

  const submitFeedback = (): void => {
    setErrors([]);
    if (!feedbackFilled()) {
      setErrors((prevState) => [...prevState, 'Please make sure to fill out all ratings']);
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
        applyServerErrors(err.data);
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
          {isAlreadyReviewed && <h3>This is how you reviewed your feedback.</h3>}
          {!isAlreadyReviewed && <h3>Please take a few moments and give your feedback about the review.</h3>}
        </div>
        <div className="my-2">
          {errors.map((error) => {
            return (
              <p key={nanoid()} className="text-sm text-red-300">
                {error}
              </p>
            );
          })}
        </div>
        <ReviewFeedbackRating
          isAlreadyReviewed={isAlreadyReviewed}
          field={reviewFeedbackForm.clarity}
          addRating={addRating}
        />
        <ReviewFeedbackRating
          isAlreadyReviewed={isAlreadyReviewed}
          field={reviewFeedbackForm.helpfulness}
          addRating={addRating}
        />
        <ReviewFeedbackRating
          isAlreadyReviewed={isAlreadyReviewed}
          field={reviewFeedbackForm.thoroughness}
          addRating={addRating}
        />
        <ReviewFeedbackRating
          isAlreadyReviewed={isAlreadyReviewed}
          field={reviewFeedbackForm.responseTime}
          addRating={addRating}
        />
      </div>
      {!isAlreadyReviewed && (
        <div className="flex justify-center">
          <button onClick={submitFeedback} className="btn">
            Submit Feedback
          </button>
        </div>
      )}
    </div>
  );
};

export default ReviewFeedbackRoute;
