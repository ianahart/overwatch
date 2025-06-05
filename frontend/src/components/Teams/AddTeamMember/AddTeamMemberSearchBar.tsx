import { useCallback, useState } from 'react';
import { debounce } from 'lodash';
import { useSelector } from 'react-redux';
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

import { IReviewer } from '../../../interfaces';
import { TRootState, useCreateTeamInvitationMutation, useLazyFetchReviewersQuery } from '../../../state/store';
import Avatar from '../../Shared/Avatar';
import { useParams } from 'react-router-dom';

const AddTeamMemberSearchBar = () => {
  const paginationState = {
    page: -1,
    pageSize: 2,
    totalPages: 0,
    direction: 'next',
    totalElements: 0,
  };
  const { teamId } = useParams();
  const { token, user } = useSelector((store: TRootState) => store.user);
  const [pag, setPag] = useState(paginationState);
  const [inputValue, setInputValue] = useState('');
  const [error, setError] = useState('');
  const [reviewers, setReviewers] = useState<IReviewer[]>([]);
  const [fetchReviewers] = useLazyFetchReviewersQuery();
  const [createTeamInvitation] = useCreateTeamInvitationMutation();

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

  const initiateToast = (reviewerName: string): void => {
    toast.success(`You sent a team invitation to ${reviewerName}!`, {
      position: 'bottom-center',
      autoClose: 5000,
      hideProgressBar: false,
      closeOnClick: true,
      pauseOnHover: true,
      draggable: true,
      progress: undefined,
      theme: 'dark',
    });
  };

  const handleOnClick = (reviewer: IReviewer): void => {
    const payload = { token, senderId: user.id, receiverId: reviewer.id, teamId: Number.parseInt(teamId as string) };

    createTeamInvitation(payload)
      .unwrap()
      .then(() => {
        initiateToast(reviewer.fullName);
      })
      .catch((err) => {
        setError(err.data.message);
      });
  };

  return (
    <div className="flex flex-col">
      {error.length > 0 && (
        <div>
          <p className="text-sm text-red-300">{error}</p>
        </div>
      )}
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
              <div
                data-testid="searchbar-reviewer"
                className="cursor-pointer my-2"
                onClick={() => handleOnClick(reviewer)}
                key={reviewer.id}
              >
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
      <ToastContainer />
    </div>
  );
};

export default AddTeamMemberSearchBar;
