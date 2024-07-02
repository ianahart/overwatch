import { IMinProfile } from '../../interfaces';
import Reviewer from './Reviewer';

export interface IReviewersProps {
  reviewers: IMinProfile[];
  filterValue: string;
  updateFavoritedReviewer: (id: number, isFavorited: boolean) => void;
}

const Reviewers = ({ reviewers, filterValue, updateFavoritedReviewer }: IReviewersProps) => {
  return (
    <div className="my-8">
      {reviewers.map((reviewer) => {
        return (
          <Reviewer
            updateFavoritedReviewer={updateFavoritedReviewer}
            filterValue={filterValue}
            key={reviewer.id}
            reviewer={reviewer}
          />
        );
      })}
    </div>
  );
};

export default Reviewers;
