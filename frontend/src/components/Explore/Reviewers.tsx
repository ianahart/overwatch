import { IMinProfile } from '../../interfaces';
import Reviewer from './Reviewer';

export interface IReviewersProps {
  reviewers: IMinProfile[];
  filterValue: string;
}

const Reviewers = ({ reviewers, filterValue }: IReviewersProps) => {
  return (
    <div className="my-8">
      {reviewers.map((reviewer) => {
        return <Reviewer filterValue={filterValue} key={reviewer.id} reviewer={reviewer} />;
      })}
    </div>
  );
};

export default Reviewers;
