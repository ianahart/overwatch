import { useSelector } from 'react-redux';
import Header from '../Header';
import { TRootState } from '../../../state/store';
import { useMemo } from 'react';
import NotificationSwitch from './NotificationSwitch';
import { nanoid } from 'nanoid';

const Notification = () => {
  const { setting } = useSelector((store: TRootState) => store.setting);
  const switches = useMemo(() => {
    return Object.entries(setting).reduce((notifArray, [key, value]) => {
      if (key.endsWith('On')) {
        notifArray.push({ key, value: value as boolean });
      }
      return notifArray;
    }, [] as Array<{ key: string; value: boolean }>);
  }, [setting]);

  return (
    <>
      <Header heading="Notifications" />
      <ul className="my-6">
        {switches.map(({ key, value }) => {
          return (
            <li data-testid="notification-switch-item" key={nanoid()} className="my-4">
              <NotificationSwitch propName={key} value={value} setting={setting} />
            </li>
          );
        })}
      </ul>
    </>
  );
};

export default Notification;
