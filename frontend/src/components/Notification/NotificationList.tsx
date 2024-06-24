import { INotification } from '../../interfaces';
import dayjs from 'dayjs';

import relativeTime from 'dayjs/plugin/relativeTime';
import { AiOutlineClose } from 'react-icons/ai';
import Avatar from '../Shared/Avatar';
import { NotificationRole, NotificationType } from '../../enums';

dayjs.extend(relativeTime);

export interface INotificationListProps {
  notifications: INotification[];
  handleDeleteNotification: (notification: INotification) => void;
}

const NotificationList = ({ notifications, handleDeleteNotification }: INotificationListProps) => {
  return (
    <div className="p-2">
      {notifications.map((notification) => {
        return (
          <div key={notification.id} className="my-2">
            <div className="flex justify-end">
              <AiOutlineClose onClick={() => handleDeleteNotification(notification)} className="text-gray-400" />
            </div>
            <div className="flex items-center">
              <Avatar initials="?.?" width="w-6" height="h-6" avatarUrl={notification.avatarUrl} />
              <p className="text-xs ml-2 text-gray-400">{notification.text}</p>
            </div>
            {notification.notificationRole === NotificationRole.RECEIVER &&
              notification.notificationType === NotificationType.CONNECTION_REQUEST_PENDING && (
                <div className="flex justify-center">
                  <button className="bg-blue-400 text-xs rounded-lg p-2 text-black mx-1">Accept</button>
                                    <button onClick={() => handleDeleteNotification(notification)} className="bg-gray-400 text-xs rounded-lg p-2 text-black mx-1">Deny</button>
                </div>
              )}
            <div className="flex justify-end">
              <p className="text-xs italic">{dayjs().to(dayjs(notification.createdAt))}</p>
            </div>
          </div>
        );
      })}
    </div>
  );
};

export default NotificationList;
