import { useDispatch, useSelector } from 'react-redux';
import { ISetting } from '../../../interfaces';
import { TRootState, updateSetting, useUpdateSettingsMutation } from '../../../state/store';

export interface INotificationSwitchProps {
  propName: string;
  value: boolean;
  setting: ISetting;
}

export interface INotifDetailsMapper {
  reviewInProgressNotifOn: { name: string; details: string };
  reviewInCompleteNotifOn: { name: string; details: string };
  reviewCompletedNotifOn: { name: string; details: string };
  paymentAcknowledgementNotifOn: { name: string; details: string };
  requestPendingNotifOn: { name: string; details: string };
  requestAcceptedNotifOn: { name: string; details: string };
  commentReplyOn: { name: string; details: string };
}

const NotificationSwitch = ({ propName, value, setting }: INotificationSwitchProps) => {
  const { token } = useSelector((store: TRootState) => store.user);
  const [updateSettingsMut] = useUpdateSettingsMutation();
  const dispatch = useDispatch();
  const handleOnChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    console.log(e.target.checked);
    const updatedSetting = { ...setting, [propName]: e.target.checked };
    updateSettingsMut({ setting: updatedSetting, token })
      .unwrap()
      .then((res) => {
        if (res.data !== null || res.data !== undefined) {
          console.log(res.data);
          dispatch(updateSetting(res.data));
        }
      })
      .catch((err) => {
        console.log(err);
      });
  };

  const notifDetailsMapper: INotifDetailsMapper = {
    reviewInProgressNotifOn: {
      name: 'Review inprogress notifications',
      details:
        'Toggling this on and off will result in you seeing notifications about your code reviews that are currently in progress.',
    },
    reviewInCompleteNotifOn: {
      name: 'Review incomplete notifications',
      details:
        'Toggling this on and off will result in you seeing notifications about your code reviews that have not yet been started, but received.',
    },
    reviewCompletedNotifOn: {
      name: 'Review complete notifications',
      details:
        'Toggling this on and off will result in you seeing notifications regarding your code reviews that have been completed.',
    },
    paymentAcknowledgementNotifOn: {
      name: 'Payment notifications',
      details:
        'Toggling this on and off will result in you seeing notifications regarding payments and transactions on OverWatch',
    },
    requestPendingNotifOn: {
      name: 'Connection pending notifications',
      details:
        'Toggling this on and off will result in you seeing notifications regarding pending connection requests.',
    },
    requestAcceptedNotifOn: {
      name: 'Connection accepted notifications',
      details:
        'Toggling this on and off will result in you seeing notifications regarding accepted connection requests.',
    },
    commentReplyOn: {
      name: 'Reply To Comments',
      details: "Toggling this on and off will result in you not getting notifications if you're comment is replied to.",
    },
  };

  const notifName = propName as keyof INotifDetailsMapper;

  return (
    <div className="flex items-center">
      <label htmlFor={`switch${propName}`} className="relative flex justify-between items-center group p-2 text-xl">
        <input
          id={`switch${propName}`}
          name={`switch${propName}`}
          onChange={handleOnChange}
          checked={value}
          type="checkbox"
          className="absolute left-1/2 -translate-x-1/2 w-full h-full peer appearance-none rounded-md"
        />
        <span className="w-16 h-10 flex items-center flex-shrink-0 ml-4 p-1 bg-gray-300 rounded-full duration-300 ease-in-out peer-checked:bg-green-400 after:w-8 after:h-8 after:bg-white after:rounded-full after:shadow-md after:duration-300 peer-checked:after:translate-x-6 group-hover:after:translate-x-1"></span>
      </label>

      <div className="flex-col">
        <h4 className="text-lg text-gray-400">{notifDetailsMapper[notifName].name}</h4>
        <p className="text-sm">{notifDetailsMapper[notifName].details}</p>
      </div>
    </div>
  );
};

export default NotificationSwitch;
