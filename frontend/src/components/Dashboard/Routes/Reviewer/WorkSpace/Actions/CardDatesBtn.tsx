import { useEffect, useState } from 'react';
import { CiClock1 } from 'react-icons/ci';
import DatePicker from 'react-datepicker';
import { useDispatch, useSelector } from 'react-redux';
import 'react-datepicker/dist/react-datepicker.css';

import { addMonths } from '../../../../../../util/';
import ClickAway from '../../../../../Shared/ClickAway';
import { ITodoCard } from '../../../../../../interfaces';
import { TRootState, updateTodoListTodoCard, useUpdateTodoCardMutation } from '../../../../../../state/store';

export interface ICardDatesBtnProps {
  card: ITodoCard;
}

export type TDatePickerDate = Date | null;

const CardDatesBtn = ({ card }: ICardDatesBtnProps) => {
  const dispatch = useDispatch();
  const { token } = useSelector((store: TRootState) => store.user);
  const [startDate, setStartDate] = useState<TDatePickerDate>(new Date());
  const [endDate, setEndDate] = useState<TDatePickerDate>(null);
  const [updateTodoCard] = useUpdateTodoCardMutation();
  const [clickAwayOpen, setClickAwayOpen] = useState(false);

  useEffect(() => {
    if (card.startDate) {
      setStartDate(new Date(card.startDate as Date) as TDatePickerDate);
    } else {
      setStartDate(new Date());
    }
    setEndDate(card.endDate ? (card.endDate as TDatePickerDate) : null);
  }, [card.startDate, card.endDate]);

  const handleUpdateTodoCard = (payload: ITodoCard) => {
    updateTodoCard({ token, card: payload })
      .unwrap()
      .then((res) => {
        dispatch(updateTodoListTodoCard(res.data));
      })
      .catch(() => {
        console.log('error: cannot update todocard with new dates');
      });
  };

  const handleOnChange = (dates: [TDatePickerDate, TDatePickerDate]) => {
    const [start, end] = dates;
    const payload = { ...card, startDate: start as TDatePickerDate, endDate: end };
    handleUpdateTodoCard(payload);
  };

  const handleOnClickAwayOpen = () => {
    setClickAwayOpen(true);
  };

  const handleOnClickAwayClose = () => {
    setClickAwayOpen(false);
  };

  return (
    <div className="relative">
      <div className="p-1 bg-gray-800 rounded w-32 my-2 hover:bg-gray-700">
        <button onClick={handleOnClickAwayOpen} className="flex items-center text-sm">
          <CiClock1 className="mr-2" />
          Dates
        </button>
        {clickAwayOpen && (
          <ClickAway onClickAway={handleOnClickAwayClose}>
            <DatePicker
              selected={startDate}
              onChange={handleOnChange}
              minDate={new Date()}
              maxDate={addMonths(new Date(), 5)}
              startDate={startDate as Date}
              endDate={endDate as Date}
              selectsRange={true}
              inline
              showDisabledMonthNavigation
            />
          </ClickAway>
        )}
      </div>
    </div>
  );
};

export default CardDatesBtn;
