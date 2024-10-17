import { useEffect, useState } from 'react';
import { IPaginationState } from '../../../interfaces';
import { useSelector } from 'react-redux';
import { TRootState, useFetchBlockedUsersQuery } from '../../../state/store';

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
  const [];
  const { data } = useFetchBlockedUsersQuery({
    blockerUserId: user.id,
    token,
    page: -1,
    pageSize: 1,
    direction: 'next',
  });

  useEffect(() => {
    if (data !== undefined) {
      console.log(data);
    }
  }, [data]);

  return <h3>blocked users</h3>;
};

export default BlockedUserList;
