import { BsTextLeft } from 'react-icons/bs';
import { useDispatch, useSelector } from 'react-redux';
import { useEffect, useState } from 'react';

import { ITodoCard } from '../../../../../interfaces';
import { TRootState, useUpdateTodoCardMutation, updateTodoListTodoCard } from '../../../../../state/store';

export interface ICardDetailsProps {
  card: ITodoCard;
}

export interface IError {
  [index: string]: string;
}

const CardDetails = ({ card }: ICardDetailsProps) => {
  const dispatch = useDispatch();
  const { token } = useSelector((store: TRootState) => store.user);
  const [isTextareaShowing, setIsTextareaShowing] = useState(false);
  const [updateTodoCardMut] = useUpdateTodoCardMutation();
  const [details, setDetails] = useState('');
  const [error, setError] = useState('');

  useEffect(() => {
    if (card.details !== null) {
      setDetails(card.details);
    }
  }, [card.details]);

  const handleOnClick = () => {
    setIsTextareaShowing(true);
  };

  const handleOnChange = (e: React.ChangeEvent<HTMLTextAreaElement>) => {
    setDetails(e.target.value);
  };

  const applyServerErrors = <T extends IError>(data: T): void => {
    for (let prop in data) {
      setError(data[prop]);
    }
  };

  const handleOnSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    setError('');
    const updatedCard = { ...card, details };
    updateTodoCardMut({ token, card: updatedCard })
      .unwrap()
      .then((res) => {
        console.log(res);
        dispatch(updateTodoListTodoCard(res.data));
        setIsTextareaShowing(false);
      })
      .catch((err) => {
        applyServerErrors(err.data);
      });
  };

  return (
    <div className="my-8">
      <div className="flex justify-between">
        <div className="flex items-center">
          <BsTextLeft className="text-xl mr-2" />
          <h3 className="font-bold">Details</h3>
        </div>
        {!isTextareaShowing && (
          <button onClick={handleOnClick} className="p-1 bg-gray-800 rounded w-16 my-2 hover:bg-gray-700">
            Edit
          </button>
        )}
      </div>
      {!isTextareaShowing && (
        <p onClick={handleOnClick} className="cursor-pointer">
          {!card.details ? 'Write some details for your card' : card.details}
        </p>
      )}
      {isTextareaShowing && (
        <form onSubmit={handleOnSubmit}>
          <textarea
            value={details}
            onChange={handleOnChange}
            className="w-[80%] min-h-32 rounded bg-transparent border border-gray-800 resize-none"
          ></textarea>
          {error.length > 0 && <p className="text-sm text-red-300 my-1">{error}</p>}
          <div className="flex">
            <button className="btn mx-2" type="submit">
              Save
            </button>
            <button
              onClick={() => setIsTextareaShowing(false)}
              className="outline-btn mx-2 !text-gray-400"
              type="button"
            >
              Cancel
            </button>
          </div>
        </form>
      )}
    </div>
  );
};

export default CardDetails;
