import { nanoid } from '@reduxjs/toolkit';
import { IDayAvailability } from '../../interfaces';
import { AiOutlineClockCircle } from 'react-icons/ai';

export interface ITimeSlotsProps {
  availability: IDayAvailability[];
}

const TimeSlots = ({ availability }: ITimeSlotsProps) => {
  return (
    <div className="border border-t-0 border-r-0 border-gray-800 p-4">
      <h3 className="text-gray-400 text-lg flex items-center">
        Availability <AiOutlineClockCircle className="ml-1" />
      </h3>
      {availability.map(({ day, slots }) => {
        return (
          <div key={nanoid()} className="my-2">
            <p>{day}</p>
            {slots.map(({ id, startTime, endTime }) => {
              return (
                <p className="text-sm" key={id}>
                  {startTime}-{endTime}
                </p>
              );
            })}
          </div>
        );
      })}
    </div>
  );
};

export default TimeSlots;
