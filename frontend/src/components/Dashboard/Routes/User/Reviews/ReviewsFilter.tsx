import { useDispatch } from 'react-redux';
import { setRepositorySearchFilter } from '../../../../../state/store';

export interface IReviewsFilterProps {
  data: { id: string | number; value: string; name: string }[];
  id: string;
  value: string;
}

const ReviewsFilter = ({ data, id, value }: IReviewsFilterProps) => {
  const dispatch = useDispatch();
  const handleOnChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    const { name, value } = e.target;
    dispatch(setRepositorySearchFilter({ name, value }));
  };

  return (
    <div className="m-2">
      <select
        id={id}
        name={id}
        value={value}
        onChange={handleOnChange}
        className="w-full md:w-[200px] bg-transparent border-gray-800 rounded border text-gray-400 text-sm p-2"
      >
        {data.map(({ id, value, name }) => {
          return (
            <option key={id} value={value}>
              {name}
            </option>
          );
        })}
      </select>
    </div>
  );
};

export default ReviewsFilter;
