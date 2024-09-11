import { BsTextLeft } from 'react-icons/bs';
import { useDispatch, useSelector } from 'react-redux';
import { useState } from 'react';
import { ITodoCard } from '../../../../../interfaces';
import { TRootState, useUpdateTodoCardMutation, updateTodoListTodoCard } from '../../../../../state/store';
import DetailsEditor from './DetailsEditor';
import { BaseEditor } from 'slate';
import { TCustomElement, TCustomText } from '../../../../../types';
import { ReactEditor } from 'slate-react';
import DisplayEditor from './DisplayEditor';

export interface ICardDetailsProps {
  card: ITodoCard;
}

export interface IError {
  [index: string]: string;
}

declare module 'slate' {
  interface CustomTypes {
    Editor: BaseEditor & ReactEditor;
    Element: TCustomElement;
    Text: TCustomText;
  }
}

const CardDetails = ({ card }: ICardDetailsProps) => {
  const dispatch = useDispatch();
  const { token } = useSelector((store: TRootState) => store.user);
  const [isTextareaShowing, setIsTextareaShowing] = useState(false);
  const [updateTodoCardMut] = useUpdateTodoCardMutation();
  const [error, setError] = useState('');

  const handleOnClick = () => {
    setIsTextareaShowing(true);
  };

  const applyServerErrors = <T extends IError>(data: T): void => {
    for (let prop in data) {
      setError(data[prop]);
    }
  };

  const handleOnSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    setError('');
    const LSdetails = localStorage.getItem('details') ?? '';
    const updatedCard = { ...card, details: LSdetails };
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
        <div onClick={handleOnClick} className="cursor-pointer">
          {!card.details ? 'Write some details for your card' : <DisplayEditor details={card.details} />}
        </div>
      )}
      {isTextareaShowing && (
        <form onSubmit={handleOnSubmit}>
          <DetailsEditor details={card.details} />
          {error.length > 0 && <p className="text-sm text-red-300 my-1">{error}</p>}
          <div className="flex">
            <button className="btn mx-2" type="submit">
              Save
            </button>
            <button
              onClick={() => {
                setIsTextareaShowing(false);
                localStorage.removeItem('details');
              }}
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
