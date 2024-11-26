import { useCallback, useState } from 'react';
import { debounce } from 'lodash';
import { useSelector } from 'react-redux';

import { IReviewer } from '../../../interfaces';
import { TRootState, useLazyFetchReviewersQuery } from '../../../state/store';
import Avatar from '../../Shared/Avatar';

const AddTeamMemberSearchBar = () => {
  const paginationState = {
    page: -1,
    pageSize: 2,
    totalPages: 0,
    direction: 'next',
    totalElements: 0,
  };
  const { token } = useSelector((store: TRootState) => store.user);
  const [pag, setPag] = useState(paginationState);
  const [inputValue, setInputValue] = useState<string>('');
  const [reviewers, setReviewers] = useState<IReviewer[]>([]);
  const [fetchReviewers] = useLazyFetchReviewersQuery();

  const preformDebounce = debounce((query) => {
    applySearch(query, false);
  }, 250);

  const handleOnChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const searchValue = e.target.value;
    if (searchValue.length === 0) {
      setReviewers([]);
      setPag({ page: 0, pageSize: 2, totalPages: 0, direction: 'next', totalElements: 0 });
    }
    debouncedSearch(searchValue);
    setInputValue(searchValue);
  };

  const debouncedSearch = useCallback((query: string) => preformDebounce(query), []);

  const applySearch = (query: string, paginate: boolean) => {
    const pageNum = paginate ? pag.page : -1;
    if (query.trim().length === 0) return;
    fetchReviewers({ token, search: query, page: pageNum, pageSize: pag.pageSize, direction: pag.direction })
      .unwrap()
      .then((res) => {
        const { items, page, pageSize, totalPages, direction, totalElements } = res.data;
        setPag((prevState) => ({
          ...prevState,
          page,
          pageSize,
          totalElements,
          totalPages,
          direction,
        }));
        if (paginate) {
          setReviewers((prevState) => [...prevState, ...items]);
        } else {
          setReviewers(items);
        }
      })
      .catch((err) => console.log(err));
  };

  const handleOnClick = (reviewer: IReviewer): void => {
    console.log(reviewer);
  };

  return (
    <div className="flex flex-col">
      <label htmlFor="search">Lookup a reviewer</label>
      <div className="w-full">
        <input
          value={inputValue}
          onChange={handleOnChange}
          className="h-9 bg-transparent border border-gray-800 rounded w-full md:w-[75%]"
          name="search"
          id="search"
        />
      </div>
      {reviewers.length > 0 && (
        <div className="my-4 bg-stone-950 p-4">
          {reviewers.map((reviewer) => {
            return (
              <div className="cursor-pointer my-2" onClick={() => handleOnClick(reviewer)} key={reviewer.id}>
                <div className="flex items-center">
                  <Avatar initials="?.?" avatarUrl={reviewer.avatarUrl} width="w-6" height="h-6" />
                  <p className="text-sm text-gray-400">{reviewer.fullName}</p>
                </div>
              </div>
            );
          })}
          {pag.page < pag.totalPages - 1 && (
            <div onClick={() => applySearch(inputValue, true)} className="my-2 flex justify-center">
              <button className="text-center text-xs">See more...</button>
            </div>
          )}
        </div>
      )}
    </div>
  );
};

export default AddTeamMemberSearchBar;
