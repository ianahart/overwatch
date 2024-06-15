import { useLocation, useParams } from 'react-router-dom';
import ActionReview from './ActionReview';

const EditReview = () => {
  const { authorId, reviewerId, avatarUrl, fullName } = useLocation().state;
  const params = useParams();
  const reviewId = params.reviewId as string;
  return (
    <ActionReview
      reviewId={Number.parseInt(reviewId)}
      action="edit"
      authorId={authorId}
      reviewerId={reviewerId}
      avatarUrl={avatarUrl}
      fullName={fullName}
    />
  );
};

export default EditReview;
