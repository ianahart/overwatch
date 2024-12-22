import { useEffect } from 'react';
import { useSelector } from 'react-redux';
import { TRootState, useFetchBlockedUsersQuery } from '../../../state/store';

const BlockedUserList = () => {
  const { token, user } = useSelector((store: TRootState) => store.user);
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
