import { useEffect, useState } from 'react';
import { useSelector } from 'react-redux';
import { useNavigate, useParams } from 'react-router-dom';
import FilterControls from './FilterControls';
import { paginationState } from '../../data';
import { TRootState, useLazyFetchAllProfileQuery } from '../../state/store';
import { IMinProfile } from '../../interfaces';

const initialDescriptionState = 'Browse Reviewers that have just signed up and our new to the platform.';

const Explore = () => {
  const { token } = useSelector((store: TRootState) => store.user);
  const [fetchAllProfiles] = useLazyFetchAllProfileQuery();
  const params = useParams();
  const navigate = useNavigate();
  const [profiles, setProfiles] = useState<IMinProfile[]>([]);
  const [pag, setPag] = useState(paginationState);
  const [filter, setFilter] = useState({
    value: params.filter as string,
    desc: initialDescriptionState,
  });

  const fetchProfiles = async (paginate: boolean, filterValue: string) => {
    try {
      const pageNum = paginate ? pag.page : -1;
      const params = { token, page: pageNum, direction: 'next', pageSize: 1, filter: filterValue };
      const response = await fetchAllProfiles(params).unwrap();
      const { items, direction, page, pageSize, totalElements, totalPages } = response.data;

      setPag((prevState) => ({
        ...prevState,
        direction,
        page,
        pageSize,
        totalElements,
        totalPages,
      }));
      setProfiles((prevState) => [...prevState, ...items]);
      console.log(response);
    } catch (err) {
      console.log(err);
    }
  };

  useEffect(() => {
    navigate(`/explore/${filter.value}`);
        setProfiles([])
  }, [filter.value]);

  useEffect(() => {
    fetchProfiles(false, filter.value);
  }, []);

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
            <FilterControls fetchProfiles={fetchProfiles} handleSetFilter={handleSetFilter} filter={filter} />
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
