import { useEffect, useState } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { AiOutlineSearch } from 'react-icons/ai';

import { paginationState } from '../../../data';
import {
  TRootState,
  useFetchConnectionsQuery,
  setConnections,
  setCurrentConnection,
  useLazyFetchConnectionsQuery,
  clearChat,
} from '../../../state/store';
import Avatar from '../../Shared/Avatar';
import Spinner from '../../Shared/Spinner';
import { IConnection } from '../../../interfaces';

const Connections = () => {
  const dispatch = useDispatch();
  const { user, token } = useSelector((store: TRootState) => store.user);
  const { connections, currentConnection } = useSelector((store: TRootState) => store.chat);
  const [pag, setPag] = useState(paginationState);
  const [fetchConnections, { isLoading: fetchLoading }] = useLazyFetchConnectionsQuery();

  const { data, isLoading } = useFetchConnectionsQuery({
    userId: user.id,
    token,
    page: -1,
    pageSize: 3,
    direction: 'next',
  });

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
      dispatch(setConnections(items));
      if (items.length > 0) {
        dispatch(setCurrentConnection(items[0]));
      }
    }
  }, [data, dispatch]);

  const paginateConnections = async (dir: string) => {
    try {
      if (fetchLoading) return;

      const response = await fetchConnections({
        userId: user.id,
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
      dispatch(setConnections(items));
    } catch (err) {
      console.log(err);
    }
  };

  const changeConnection = (connection: IConnection) => {
    dispatch(setCurrentConnection(connection));
  };

  return (
    <div className="p-2">
      <div className="my-4">
        <div className="relative">
          <input placeholder="Search" className=" pl-7 w-full h-9 bg-transparent rounded border border-gray-800" />
          <AiOutlineSearch className="absolute top-2 left-2 text-xl" />
        </div>
      </div>
      {isLoading && (
        <div className="flex justify-center my-8">
          <Spinner message="Loading connections..." />
        </div>
      )}
      <div className="my-8">
        {connections.map((connection) => {
          return (
            <div
              onClick={() => changeConnection(connection)}
              key={connection.id}
              className={`my-4 hover:bg-stone-950 hover:text-gray-400 cursor-pointer ${
                currentConnection.id === connection.id ? 'bg-green-400 text-black' : 'bg-transparent text-gray-400'
              }`}
            >
              <div className="flex items-center">
                <Avatar initials="?.?" width="h-9" height="h-9" avatarUrl={connection.avatarUrl} />
                <p className="ml-2">
                  {connection.firstName} {connection.lastName}
                </p>
              </div>
            </div>
          );
        })}
      </div>
      {pag.page < pag.totalPages - 1 && (
        <div className="my-8 flex justify-center">
          <button onClick={() => paginateConnections('next')}>See more...</button>
        </div>
      )}
    </div>
  );
};

export default Connections;
