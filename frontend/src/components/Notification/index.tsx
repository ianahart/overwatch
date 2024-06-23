import { useEffect, useRef, useState } from 'react';
import { over } from 'stompjs';
import SockJS from 'sockjs-client';
import { AiOutlineBell } from 'react-icons/ai';
import { useSelector } from 'react-redux';
import { TRootState } from '../../state/store';

let stompClient: any = null;

const Notifications = () => {
  const { user } = useSelector((store: TRootState) => store.user);
  const [isNotificationsOpen, setIsNotificationsOpen] = useState(false);
  const menuRef = useRef<HTMLDivElement>(null);
  const triggerRef = useRef<HTMLDivElement>(null);

  const clickAway = (e: MouseEvent) => {
    const target = e.target as Element;

    if (menuRef.current !== null && triggerRef.current !== null) {
      if (!menuRef.current.contains(target) && !triggerRef.current.contains(target)) {
        setIsNotificationsOpen(false);
      }
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
    console.log('Run');
    console.log(message.body);
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

  return (
    <div className="relative">
      <div ref={triggerRef}>
        <AiOutlineBell onClick={() => setIsNotificationsOpen((prevState) => !prevState)} className="text-2xl mr-2" />
      </div>
      {isNotificationsOpen && (
        <div
          ref={menuRef}
          className="h-36 overflow-y-auto absolute top-8 z-10 rounded -left-64 md:w-[300px] w-[180px] bg-gray-900 shadow-md"
        >
          asds
        </div>
      )}
    </div>
  );
};

export default Notifications;
