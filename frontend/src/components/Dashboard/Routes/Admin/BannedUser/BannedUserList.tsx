import { useEffect, useState } from 'react';
import { IBan, IPaginationState } from '../../../../../interfaces';
import { useSelector } from 'react-redux';
import { TRootState, useFetchBannedUsersQuery, useLazyFetchBannedUsersQuery } from '../../../../../state/store';
import BannedUserListItem from './BannedUserListItem';

export interface IBannedUserListProps {
  handleSetView: (view: string) => void;
}

const BannedUserList = ({ handleSetView }: IBannedUserListProps) => {
  const paginationState = {
    page: 0,
    pageSize: 10,
    totalPages: 0,
    direction: 'next',
    totalElements: 0,
  };
  const { token } = useSelector((store: TRootState) => store.user);
  const [pag, setPag] = useState<IPaginationState>(paginationState);
  const [users, setUsers] = useState<IBan[]>([]);
  const [fetchBannedUsers] = useLazyFetchBannedUsersQuery();
  const { data } = useFetchBannedUsersQuery(
    {
      token,
      page: -1,
      pageSize: 10,
      direction: 'next',
    },
    { skip: !token }
  );

  useEffect(() => {
    if (data !== undefined) {
      const { items, page, pageSize, totalPages, direction, totalElements } = data.data;
      setPag((prevState) => ({
        ...prevState,
        page,
        pageSize,
        totalElements,
        totalPages,
        direction,
      }));
      setUsers(items);
    }
  }, [data]);

  const paginateBannedUsers = async (dir: string) => {
    try {
      const response = await fetchBannedUsers({
        token,
        page: pag.page,
        pageSize: pag.pageSize,
        direction: dir,
      }).unwrap();

      const { items, page, pageSize, totalPages, direction, totalElements } = response.data;
      setPag((prevState) => ({
        ...prevState,
        page,
        pageSize,
        totalElements,
        totalPages,
        direction,
      }));
      setUsers(items);
    } catch (err) {
      console.log(err);
    }
  };

  const updateBannedUserState = (ban: IBan): void => {
    setUsers(
      users.map((user) => {
        if (user.id === ban.id) {
          return { ...ban };
        } else {
          return { ...user };
        }
      })
    );
  };

  const removeBannedUserState = (banId: number): void => {
    setUsers(users.filter((user) => user.id !== banId));
  };

  return (
    <>
      <div className="my-4 flex justify-center">
        <h3 className="text-xl text-gray-400 font-bold">Banned Users</h3>
      </div>
      <div className="my-8 overflow-x-auto">
        {users.map((user) => {
          return (
            <BannedUserListItem
              key={user.id}
              user={user}
              handleSetView={handleSetView}
              updateBannedUserState={updateBannedUserState}
              removeBannedUserState={removeBannedUserState}
            />
          );
        })}
      </div>
      <div className="flex items-center justify-center">
        {pag.page > 0 && (
          <button onClick={() => paginateBannedUsers('prev')} className="mx-1">
            Prev
          </button>
        )}
        <p className="text-green-400">
          {pag.page + 1} of {pag.totalPages}
        </p>
        {pag.page < pag.totalPages - 1 && (
          <button onClick={() => paginateBannedUsers('next')} className="mx-1">
            Next
          </button>
        )}
      </div>
    </>
  );
};

export default BannedUserList;
