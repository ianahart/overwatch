import { useEffect, useState } from 'react';
import { IGetAllAdminUsersResponse, IPaginationState, IViewUser } from '../../../../../interfaces';
import { useSelector } from 'react-redux';
import { TRootState, useFetchAllUsersQuery, useLazyFetchAllUsersQuery } from '../../../../../state/store';
import UserListItem from './UserListItem';

const AllUser = () => {
  const paginationState = {
    page: 0,
    pageSize: 10,
    totalPages: 0,
    direction: 'next',
    totalElements: 0,
  };

  const { token } = useSelector((store: TRootState) => store.user);
  const [pag, setPag] = useState<IPaginationState>(paginationState);
  const [users, setUsers] = useState<IViewUser[]>([]);
  const [fetchUsers] = useLazyFetchAllUsersQuery();
  const { data } = useFetchAllUsersQuery(
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
      handleSetUsers(data);
    }
  }, [data]);

  const handleSetUsers = (res: IGetAllAdminUsersResponse): void => {
    const { direction, items, page, pageSize, totalElements, totalPages } = res.data;
    setPag((prevState) => ({
      ...prevState,
      page,
      pageSize,
      direction,
      totalPages,
      totalElements,
    }));
    setUsers(items);
  };

  const paginateUsers = (dir: string): void => {
    const payload = {
      token,
      page: pag.page,
      pageSize: pag.pageSize,
      direction: dir,
    };

    fetchUsers(payload)
      .unwrap()
      .then((res) => {
        handleSetUsers(res);
      })
      .catch((err) => {
        console.log(err);
      });
  };

  return (
    <div className="md:max-w-[1450px] w-full mx-auto mt-8">
      <div className="border border-gray-800 p-2 rounded">
        <h2 className="text-gray-400 text-xl">View All Users</h2>
        <div className="my-8">
          {users.map((user) => {
            return <UserListItem key={user.id} user={user} />;
          })}
        </div>
        <div className="flex items-center text-gray-400 justify-center">
          {pag.page > 0 && (
            <button onClick={() => paginateUsers('prev')} className="mx-2">
              Prev
            </button>
          )}
          <p className="mx-2">{pag.page + 1}</p>
          {pag.page < pag.totalPages - 1 && (
            <button onClick={() => paginateUsers('next')} className="mx-2">
              Next
            </button>
          )}
        </div>
      </div>
    </div>
  );
};

export default AllUser;
