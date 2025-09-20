import { debounce } from 'lodash';
import { useCallback, useState } from 'react';
import { useSelector } from 'react-redux';
import { TRootState, useLazyFetchAllUserAndReviewersQuery } from '../../../../../state/store';
import { paginationState } from '../../../../../data';
import { IReviewer } from '../../../../../interfaces';
import ClickAway from '../../../../Shared/ClickAway';
import Avatar from '../../../../Shared/Avatar';
import { initializeName } from '../../../../../util';

export interface IBannedUserFormSearchProps {
  handleSetSelectedUser: (user: IReviewer) => void;
}

const BannedUserFormSearch = ({ handleSetSelectedUser }: IBannedUserFormSearchProps) => {
  const { token } = useSelector((store: TRootState) => store.user);
  const [fetchAllUserAndReviewers] = useLazyFetchAllUserAndReviewersQuery();
  const [users, setUsers] = useState<IReviewer[]>([]);
  const [clickAwayOpen, setClickAwayOpen] = useState(false);
  const [pag, setPag] = useState(paginationState);

  const [search, setSearch] = useState('');

  const searchForUsersAndReviewers = (query: string, paginate: boolean) => {
    const pageNum = paginate ? pag.page : -1;
    if (query.trim().length === 0) return;
    fetchAllUserAndReviewers({
      token,
      search: query,
      page: pageNum,
      pageSize: pag.pageSize,
      direction: pag.direction,
    })
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
          setUsers((prevState) => [...prevState, ...items]);
          setClickAwayOpen(true);
        } else {
          setUsers(items);
          setClickAwayOpen(true);
        }
      })
      .catch((err) => console.log(err));
  };

  const debouncedSearch = useCallback(
    debounce((query: string) => {
      searchForUsersAndReviewers(query, false);
    }, 300),
    []
  );

  const handleOnSearchChange = (e: React.ChangeEvent<HTMLInputElement>): void => {
    const { value } = e.target;
    setSearch(value);
    debouncedSearch(value);
  };

  const handleOnKeyDown = (e: React.KeyboardEvent<HTMLInputElement>): void => {
    if (e.key.toLowerCase() === 'backspace' && search.trim().length === 1) {
      setUsers([]);
      setPag(paginationState);
    }
  };

  const handleClickAway = (): void => {
    setClickAwayOpen(false);
  };

  return (
    <div className="my-4 flex flex-col relative">
      <label htmlFor="search">Select a user</label>
      <input
        className="h-9 border rounded border-gray-800 p-1 bg-transparent"
        placeholder="Enter a user or reviewer's name..."
        onChange={handleOnSearchChange}
        onKeyDown={handleOnKeyDown}
        value={search}
        name="search"
        id="search"
      />
      {clickAwayOpen && (
        <ClickAway onClickAway={handleClickAway}>
          <div className="bg-gray-950 rounded absolute w-full border border-gray-800">
            {users.map(({ id, fullName, avatarUrl }) => {
              return (
                <div
                  data-testid="banned-user-search-item"
                  onClick={() => handleSetSelectedUser({ id, fullName, avatarUrl })}
                  key={id}
                  className="my-2 p-1 flex items-center justify-between cursor-pointer"
                >
                  <div className="flex items-center">
                    <Avatar
                      width="w-9"
                      initials={initializeName(fullName.split(' ')[0], fullName.split(' ')[1])}
                      height="h-9"
                      avatarUrl={avatarUrl}
                    />
                    <p className="text-gray-400 ml-1">{fullName}</p>
                  </div>
                  <div>
                    <p>User Id: {id}</p>
                  </div>
                </div>
              );
            })}
            {pag.page < pag.totalPages - 1 && (
              <button onClick={() => searchForUsersAndReviewers(search, true)} className="text-xs p-1">
                Load more...
              </button>
            )}
          </div>
        </ClickAway>
      )}
    </div>
  );
};

export default BannedUserFormSearch;
