import { useDispatch, useSelector } from 'react-redux';
import { useState } from 'react';
import dayjs from 'dayjs';
import utc from 'dayjs/plugin/utc';
import timezone from 'dayjs/plugin/timezone';
dayjs.extend(utc);
dayjs.extend(timezone);

import { TRootState, addTimeSlot, removeTimeSlot, updateMoreInfo } from '../../../state/store';
import Header from '../Header';
import FormTextareaField from '../../Form/FormTextareaField';

interface IAddSlotFormProps {
  day: string;
  handleOnAddSlot: (day: string, startTime: string, endTime: string) => void;
}

const AdditionalInfo = () => {
  const userTimezone = dayjs.tz.guess();
  const dispatch = useDispatch();
  const { availability } = useSelector((store: TRootState) => store.additionalInfo);

  const handleOnRemoveSlot = (day: string, id: string) => {
    dispatch(removeTimeSlot({ day, id }));
  };

  const handleOnAddSlot = (day: string, sTime: string, eTime: string) => {
    const startTime = formatTime(sTime, userTimezone);
    const endTime = formatTime(eTime, userTimezone);
    dispatch(addTimeSlot({ day, startTime, endTime }));
  };

  const formatTime = (time: string, userTimezone: string) => {
    const [hour, minute] = time.split(':');
    const combined = dayjs()
      .startOf('day')
      .hour(parseInt(hour))
      .minute(parseInt(minute))
      .second(0)
      .millisecond(0)
      .tz(userTimezone);
    return combined.format('hh:mm A');
  };

  return (
    <div>
      <Header heading="Additional Info" />
      <div className="my-4">
        <h3 className="text-gray-400 text-xl">Time availability</h3>
      </div>
      {availability.map(({ day, slots }) => (
        <div key={day}>
          <h3>{day}</h3>
          {slots.map((slot) => (
            <div key={slot.id} className="my-2">
              <span>
                {slot.startTime} - {slot.endTime}
              </span>
              <button
                className="outline-btn ml-2 !text-gray-500 border border-gray-800 rounded"
                type="button"
                onClick={() => handleOnRemoveSlot(day, slot.id)}
              >
                Remove
              </button>
            </div>
          ))}
          <AddSlotForm day={day} handleOnAddSlot={handleOnAddSlot} />
        </div>
      ))}
      <div className="my-4">
        <h3 className="text-xl text-gray-400">More Info</h3>
        <textarea
          id="moreInfo"
          name="moreInfo"
          onChange={(e) => dispatch(updateMoreInfo(e.target.value))}
          className="h-24 border-gray-800 rounded border bg-transparent md:w-[60%] w-full"
        ></textarea>
      </div>
    </div>
  );
};

const AddSlotForm: React.FC<IAddSlotFormProps> = ({ day, handleOnAddSlot }) => {
  const [startTime, setStartTime] = useState<string>('');
  const [endTime, setEndTime] = useState<string>('');

  const handleAdd = () => {
    if (startTime && endTime) {
      handleOnAddSlot(day, startTime, endTime);
      setStartTime('');
      setEndTime('');
    }
  };

  return (
    <div className="my-2">
      <input
        className="h-9 mx-1 rounded bg-transparent border border-gray-800 md:w-[20%] placeholder:pl-2 pl-2 shadow"
        type="time"
        value={startTime}
        onChange={(e) => setStartTime(e.target.value)}
      />
      <input
        className="h-9 mx-1 rounded bg-transparent border border-gray-800 md:w-[20%] placeholder:pl-2 pl-2 shadow"
        type="time"
        value={endTime}
        onChange={(e) => setEndTime(e.target.value)}
      />
      <button type="button" onClick={handleAdd}>
        Add Slot
      </button>
    </div>
  );
};

export default AdditionalInfo;
