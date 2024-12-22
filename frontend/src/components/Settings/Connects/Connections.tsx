import { useEffect, useRef, useState } from 'react';
import { useSelector, useDispatch } from 'react-redux';

import { paginationState } from '../../../data';
import {
  TRootState,
  useFetchConnectionsQuery,
  setConnections,
  setCurrentConnection,
  useLazyFetchConnectionsQuery,
  clearMessages,
  useFetchPinnedConnectionsQuery,
  setPinnedConnections,
  useDeletePinnedConnectionMutation,
  removePinnedConnection,
  useCreatePinnedConnectionMutation,
  removeConnection,
  useLazyFetchPinnedConnectionsQuery,
  clearPinnedConnections,
  clearConnections,
  useCreateBlockedUserMutation,
} from '../../../state/store';
import Spinner from '../../Shared/Spinner';
import { IConnection } from '../../../interfaces';
import ConnectionSearch from './ConnectionSearch';
import Connection from './Connection';
import { retrieveTokens } from '../../../util';

const Connections = () => {
  const dispatch = useDispatch();
  const shouldRun = useRef(true);
  const { user, token } = useSelector((store: TRootState) => store.user);
  const { connections, currentConnection, pinnedConnections } = useSelector((store: TRootState) => store.chat);
  const [pag, setPag] = useState(paginationState);
  const [fetchConnections, { isLoading: fetchLoading }] = useLazyFetchConnectionsQuery();
  const [deletePinnedConnection] = useDeletePinnedConnectionMutation();
  const [createPinnedConnection] = useCreatePinnedConnectionMutation();
  const [createBlockedUser] = useCreateBlockedUserMutation();
  const [fetchPinnedConnections] = useLazyFetchPinnedConnectionsQuery();
  const { data: pinnedConnectionsData } = useFetchPinnedConnectionsQuery(
    {
      userId: user.id,
      token: retrieveTokens().token,
    },
    { skip: !user.id || !token }
  );

  const { data, isLoading } = useFetchConnectionsQuery(
    {
      userId: user.id,
      token,
      page: -1,
      pageSize: 3,
      direction: 'next',
      override: 'false',
    },
    { skip: !token || !user.id }
  );

  useEffect(() => {
    if (pinnedConnectionsData !== undefined) {
      dispatch(setPinnedConnections(pinnedConnectionsData.data));
    }
  }, [pinnedConnectionsData, dispatch]);

  useEffect(() => {
    if (data !== undefined && shouldRun.current) {
      shouldRun.current = false;
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
  }, [data, dispatch, shouldRun.current]);

  const paginateConnections = async (dir: string) => {
    try {
      if (fetchLoading) return;

      const response = await fetchConnections({
        userId: user.id,
        token,
        page: pag.page,
        pageSize: pag.pageSize,
        direction: dir,
        override: 'false',
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
    dispatch(clearMessages());
  };

  const blockUser = (blockerUserId: number, blockedUserId: number, connection: IConnection): void => {
    createBlockedUser({ token, blockerUserId, blockedUserId })
      .unwrap()
      .then(() => {
        dispatch(removeConnection(connection));
        dispatch(clearMessages());
      })
      .catch((err) => {
        console.log(err);
      });
  };

  const pin = (ownerId: number, connectionId: number, pinnedId: number, token: string, connection: IConnection) => {
    createPinnedConnection({ ownerId, connectionId, pinnedId, token })
      .unwrap()
      .then(() => {
        dispatch(clearPinnedConnections());
        fetchPinnedConnections({ userId: user.id, token: retrieveTokens().token })
          .unwrap()
          .then(() => {});
      })
      .catch((err) => {
        console.log(err);
      });

    dispatch(removeConnection(connection));
  };

  const unPin = (id: number) => {
    deletePinnedConnection({ connectionPinId: id, token: retrieveTokens().token })
      .unwrap()
      .then(() => {
        dispatch(removePinnedConnection(id));
        dispatch(clearConnections());

        setPag(paginationState);
      })
      .then(() => {
        fetchConnections({ userId: user.id, token, page: -1, pageSize: 3, direction: 'next', override: 'false' })
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
            dispatch(setConnections(items));
            if (items.length > 0) {
              dispatch(setCurrentConnection(items[0]));
            }
          });
      })

      .catch((err) => {
        console.log(err);
      });
  };

  return (
    <div className="p-2">
      <ConnectionSearch changeConnection={changeConnection} connectionId={currentConnection.id} />
      {isLoading && (
        <div className="flex justify-center my-8">
          <Spinner message="Loading connections..." />
        </div>
      )}
      <div className="my-12">
        {pinnedConnections.map((connection) => {
          return (
            <Connection
              key={connection.id}
              isPinned={true}
              changeConnection={changeConnection}
              connection={connection}
              unPin={unPin}
              pin={pin}
            />
          );
        })}
      </div>
      <div className="my-8">
        {connections.map((connection) => {
          return (
            <Connection
              key={connection.id}
              isPinned={false}
              changeConnection={changeConnection}
              connection={connection}
              unPin={unPin}
              pin={pin}
              blockUser={blockUser}
            />
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
