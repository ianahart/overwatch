import { useLocation } from 'react-router-dom';
import ActionReview from './ActionReview';

const CreateReview = () => {
  const { authorId, reviewerId, avatarUrl, fullName } = useLocation().state;

  return (
    <ActionReview
      action="create"
      authorId={authorId}
      reviewerId={reviewerId}
      avatarUrl={avatarUrl}
      fullName={fullName}
    />
  );
};

export default CreateReview;
