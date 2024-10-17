import { useEffect, useState } from 'react';
import { useSelector } from 'react-redux';
import dayjs from 'dayjs';
import relativeTime from 'dayjs/plugin/relativeTime';

dayjs.extend(relativeTime);

import { TRootState, useFetchBlockedUsersQuery, useLazyFetchBlockedUsersQuery } from '../../../state/store';
import { IBlockedUser, IPaginationState } from '../../../interfaces';
import BlockedUserItem from './BlockedUserItem';

const BlockedUserList = () => {
  const paginationState = {
    page: 0,
    pageSize: 1,
    totalPages: 0,
    direction: 'next',
    totalElements: 0,
  };
  const { token, user } = useSelector((store: TRootState) => store.user);
  const [pag, setPag] = useState<IPaginationState>(paginationState);
  const [blockedUsers, setBlockedUsers] = useState<IBlockedUser[]>([]);
  const [fetchBlockedUsers] = useLazyFetchBlockedUsersQuery();
  const { data } = useFetchBlockedUsersQuery({
    blockerUserId: user.id,
    token,
    page: -1,
    pageSize: 1,
    direction: 'next',
  });

  useEffect(() => {
    if (data !== undefined) {
      const { direction, items, page, pageSize, totalElements, totalPages } = data.data;
      setPag((prevState) => ({
        ...prevState,
        page,
        pageSize,
        direction,
        totalPages,
        totalElements,
      }));
      setBlockedUsers(items);
    }
  }, [data]);

  const handleOnClick = async (dir: string): Promise<void> => {
    try {
      const response = await fetchBlockedUsers({
        blockerUserId: user.id,
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
      setBlockedUsers((prevState) => [...prevState, ...items]);
    } catch (err) {
      console.log(err);
    }
  };

  return (
    <div className="max-w-[600px]">
      <h3 className="text-xl mb-4">Blocked users</h3>
      <p className="text-sm">
        These are users who you have blocked on your Connects page in messaging. To unblock them simply click the icon
        next to their name and they will be visible again in your Connects page.
      </p>
      {blockedUsers.length === 0 && <p className="my-4">You currently have no blocked users.</p>}
      <div className="my-4">
        {blockedUsers.map((blockedUser) => {
          return <BlockedUserItem key={blockedUser.id} blockedUser={blockedUser} />;
        })}
      </div>
      {pag.page < pag.totalPages - 1 && (
        <div className="my-4 p-2 rounded border-gray-800 border inline-flex">
          <button onClick={() => handleOnClick('next')}>More users</button>
        </div>
      )}
    </div>
  );
};

export default BlockedUserList;
