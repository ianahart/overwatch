import { useEffect, useRef, useState } from 'react';
import { AiOutlineBell } from 'react-icons/ai';
import { useSelector } from 'react-redux';
import {
  TRootState,
  useDeleteNotificationMutation,
  useFetchNotificationsQuery,
  useLazyFetchNotificationsQuery,
} from '../../state/store';
import { INotification, IPaginationState } from '../../interfaces';
import { paginationState } from '../../data';
import NotificationList from './NotificationList';
import { NotificationType } from '../../enums';
import { connectWebSocket, disconnectWebSocket, sendMessage, subscribeToTopic } from '../../util/WebSocketService';

const Notifications = () => {
  const { user, token } = useSelector((store: TRootState) => store.user);
  const [pag, setPag] = useState<IPaginationState>(paginationState);
  const [notifications, setNotifications] = useState<INotification[]>([]);
  const [fetchNotifications] = useLazyFetchNotificationsQuery();
  const [deleteNotification] = useDeleteNotificationMutation();

  const { data } = useFetchNotificationsQuery(
    {
      userId: user.id,
      token,
      page: -1,
      pageSize: 2,
      direction: 'next',
    },
    { skip: !token || !user.id }
  );

  const [isNotificationsOpen, setIsNotificationsOpen] = useState(false);
  const menuRef = useRef<HTMLDivElement>(null);
  const triggerRef = useRef<HTMLDivElement>(null);

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
      setNotifications(items);
    }
  }, [data]);

  const clickAway = (e: MouseEvent) => {
    const target = e.target as Element;

    if (menuRef.current !== null && triggerRef.current !== null) {
      if (!menuRef.current.contains(target) && !triggerRef.current.contains(target)) {
        setIsNotificationsOpen(false);
      }
    }
  };

  const paginateNotifications = async (dir: string) => {
    try {
      const res = await fetchNotifications({
        userId: user.id,
        token,
        page: pag.page,
        pageSize: pag.pageSize,
        direction: dir,
      }).unwrap();

      const { items, page, pageSize, totalPages, direction, totalElements } = res.data;
      setPag((prevState) => ({
        ...prevState,
        page,
        pageSize,
        totalElements,
        totalPages,
        direction,
      }));
      setNotifications((prevState) => [...prevState, ...items]);
    } catch (err) {
      console.log(err);
    }
  };

  const onConnected = () => {
    subscribeToTopic(`/user/${user.id}/topic/notifications`, onNotification);
  };

  const onNotification = () => {
    fetchNotifications({
      userId: user.id,
      token,
      page: -1,
      pageSize: 2,
      direction: 'next',
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
        setNotifications(items);
      });
  };

  const onError = () => {};

  useEffect(() => {
    if (user.id !== 0) {
      connectWebSocket(onConnected, onError);
    }
    return () => {
      disconnectWebSocket();
    };
  }, [user.id]);

  useEffect(() => {
    window.addEventListener('click', clickAway);
    return () => window.removeEventListener('click', clickAway);
  }, [clickAway]);

  const handleDeleteNotification = (notification: INotification) => {
    if (!notification) {
      return;
    }
    const { notificationRole, senderId, receiverId } = notification;
    const payload = { token, notificationId: notification.id, notificationRole, senderId, receiverId };
    deleteNotification({ ...payload }).unwrap();
  };

  const emitNotification = (notification: INotification) => {
    const { receiverId, senderId } = notification;
    const payload = { receiverId, senderId, notificationType: NotificationType.CONNECTION_REQUEST_ACCEPTED };
    sendMessage('/api/v1/notify', JSON.stringify(payload));
  };

  return (
    <div className="relative">
      <div ref={triggerRef} className="relative">
        <AiOutlineBell
          data-testid="notification-bell"
          onClick={() => setIsNotificationsOpen((prevState) => !prevState)}
          className="text-2xl mr-2"
        />
        {pag.totalElements > 0 && (
          <div className="absolute z-10 -top-2 -left-4 bg-red-400 rounded-lg p-1 w-6 h-6 flex items-center justify-center flex-col">
            <p className="text-white font-bold text-sm">{pag.totalElements > 9 ? '+9' : pag.totalElements}</p>
          </div>
        )}
      </div>
      {isNotificationsOpen && (
        <div
          data-testid="notification-outside"
          ref={menuRef}
          className="h-36 overflow-y-auto absolute top-8 z-10 rounded md:-left-64 right-0 md:w-[300px] w-[180px] bg-gray-900 shadow-md"
        >
          <NotificationList
            emitNotification={emitNotification}
            handleDeleteNotification={handleDeleteNotification}
            notifications={notifications}
          />
          {pag.page < pag.totalPages - 1 && (
            <div className="p-2 justify-center flex my-2">
              <button onClick={() => paginateNotifications('next')} className="text-xs cursor-pointer">
                More notifications...
              </button>
            </div>
          )}
        </div>
      )}
    </div>
  );
};

export default Notifications;
