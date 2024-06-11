import { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import FilterControls from './FilterControls';

const initialDescriptionState = 'Browse Reviewers that have just signed up and our new to the platform.';

const Explore = () => {
  const params = useParams();
  const navigate = useNavigate();
  const [filter, setFilter] = useState({
    value: params.filter as string,
    desc: initialDescriptionState,
  });

  useEffect(() => {
    navigate(`/explore/${filter.value}`);
  }, [filter.value]);

  const handleSetFilter = (value: string, desc: string) => {
    setFilter((prevState) => ({
      ...prevState,
      value,
      desc,
    }));
  };

  return (
    <div>
      <div className="max-w-[1280px] border mx-auto">
        <div className="border border-blue-400 max-w-[764px] m-4 p-2">
          <div className="my-4">
            <FilterControls handleSetFilter={handleSetFilter} filter={filter} />
            <div className="my-4">
              <p>{filter.desc}</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Explore;
