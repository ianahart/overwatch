import { useEffect, useRef, useState } from 'react';
import { over } from 'stompjs';
import SockJS from 'sockjs-client';
import { AiOutlineBell } from 'react-icons/ai';
import { useSelector } from 'react-redux';
import { TRootState, useFetchNotificationsQuery, useLazyFetchNotificationsQuery } from '../../state/store';
import { INotification, IPaginationState } from '../../interfaces';
import { paginationState } from '../../data';
import NotificationList from './NotificationList';

let stompClient: any = null;

const Notifications = () => {
  const { user, token } = useSelector((store: TRootState) => store.user);
  const [pag, setPag] = useState<IPaginationState>(paginationState);
  const [notifications, setNotifications] = useState<INotification[]>([]);
  const [fetchNotifications] = useLazyFetchNotificationsQuery();

  const { data } = useFetchNotificationsQuery({
    userId: user.id,
    token,
    page: -1,
    pageSize: 2,
    direction: 'next',
  });

  const [isNotificationsOpen, setIsNotificationsOpen] = useState(false);
  const menuRef = useRef<HTMLDivElement>(null);
  const triggerRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    if (data !== undefined) {
      console.log(data);
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
      const response = await fetchNotifications({
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
      setNotifications((prevState) => [...prevState, ...items]);
    } catch (err) {
      console.log(err);
    }
  };

  const connect = () => {
    if (stompClient && stompClient.connected) {
      console.log('WebSocket already connected');
      return;
    }

    let Sock = new SockJS('http://localhost:8080/ws');
    stompClient = over(Sock);

    stompClient.connect({}, onConnected, onError);
  };

  const onConnected = () => {
    stompClient.subscribe(`/user/${user.id}/topic/notifications`, onNotification);
  };

  const onNotification = (message: any) => {
    const notification = JSON.parse(message.body);
    setNotifications((prevState) => [notification, ...prevState]);
  };

  const onError = () => {};

  useEffect(() => {
    if (user.id !== 0) {
      connect();
      console.log(user.id);
    } else {
      if (stompClient !== null) {
        stompClient.disconnect();
      }
    }
  }, [user.id]);

  useEffect(() => {
    window.addEventListener('click', clickAway);
    return () => window.removeEventListener('click', clickAway);
  }, [clickAway]);

  const deleteNotification = (notification: INotification) => {
    console.log(notification);
  };

  return (
    <div className="relative">
      <div ref={triggerRef} className="relative">
        <AiOutlineBell onClick={() => setIsNotificationsOpen((prevState) => !prevState)} className="text-2xl mr-2" />
        <div className="absolute z-10 -top-2 -left-4 bg-red-400 rounded-lg p-1 w-6 h-6 flex items-center justify-center flex-col">
          <p className="text-white font-bold text-sm">{pag.totalElements}</p>
        </div>
      </div>
      {isNotificationsOpen && (
        <div
          ref={menuRef}
          className="h-36 overflow-y-auto absolute top-8 z-10 rounded md:-left-64 right-0 md:w-[300px] w-[180px] bg-gray-900 shadow-md"
        >
          <NotificationList deleteNotification={deleteNotification} notifications={notifications} />
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
