import { useState, useCallback } from 'react';
import Reviewer from '../../../src/components/Explore/Reviewer';

const ReviewerTestWrapper = ({ initialReviewer }: any) => {
  const [reviewer, setReviewer] = useState(initialReviewer);

  const updateFavoritedReviewer = useCallback(
    (id: number, isFavorited: boolean) => {
      if (id === reviewer.id) {
        setReviewer({ ...reviewer, isFavorited });
      }
    },
    [reviewer]
  );

  return <Reviewer reviewer={reviewer} filterValue="most-recent" updateFavoritedReviewer={updateFavoritedReviewer} />;
};

export default ReviewerTestWrapper;
