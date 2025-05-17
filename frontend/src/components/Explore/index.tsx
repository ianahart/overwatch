import { useEffect, useState, useRef, useCallback } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import FilterControls from './FilterControls';
import { paginationState } from '../../data';
import { useLazyFetchAllProfileQuery } from '../../state/store';
import { IMinProfile } from '../../interfaces';
import Reviewers from './Reviewers';
import { retrieveTokens } from '../../util';

const initialDescriptionState = 'Browse Reviewers that have just signed up and our new to the platform.';

const Explore = () => {
  const shouldRun = useRef(true);
  const [fetchAllProfiles] = useLazyFetchAllProfileQuery();
  const params = useParams();
  const navigate = useNavigate();
  const [reviewers, setReviewers] = useState<IMinProfile[]>([]);
  const [pag, setPag] = useState(paginationState);
  const [filter, setFilter] = useState({
    value: params.filter as string,
    desc: initialDescriptionState,
  });

  const fetchReviewers = async (paginate: boolean, filterValue: string) => {
    try {
      const pageNum = paginate ? pag.page : -1;
      const params = {
        token: retrieveTokens().token,
        page: pageNum,
        direction: 'next',
        pageSize: 2,
        filter: filterValue,
      };
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
      setReviewers((prevState) => [...prevState, ...items]);
    } catch (err) {
      console.log(err);
    }
  };

  useEffect(() => {
    navigate(`/explore/${filter.value}`);
    setReviewers([]);
  }, [filter.value]);

  useEffect(() => {
    if (shouldRun.current) {
      shouldRun.current = false;
      fetchReviewers(false, filter.value);
    }
  }, [shouldRun.current]);

  const handleSetFilter = (value: string, desc: string) => {
    setFilter((prevState) => ({
      ...prevState,
      value,
      desc,
    }));
  };

  const updateFavoritedReviewer = useCallback(
    (id: number, isFavorited: boolean) => {
      const updated = reviewers.map((reviewer) => {
        if (reviewer.id === id) {
          return { ...reviewer, isFavorited };
        } else {
          return { ...reviewer };
        }
      });
      setReviewers(updated);
    },
    [setReviewers, reviewers]
  );

  return (
    <div>
      <div className="max-w-[1280px]  mx-auto">
        <div className="max-w-[764px] m-4 p-2">
          <div className="my-4">
            <FilterControls fetchReviewers={fetchReviewers} handleSetFilter={handleSetFilter} filter={filter} />
            <div className="my-4">
              <p>{filter.desc}</p>
            </div>
          </div>
          <Reviewers
            updateFavoritedReviewer={updateFavoritedReviewer}
            filterValue={filter.value}
            reviewers={reviewers}
          />
          <div className="flex my-8">
            {pag.page < pag.totalPages - 1 && (
              <button onClick={() => fetchReviewers(true, filter.value)} className="btn w-full">
                See more
              </button>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default Explore;
