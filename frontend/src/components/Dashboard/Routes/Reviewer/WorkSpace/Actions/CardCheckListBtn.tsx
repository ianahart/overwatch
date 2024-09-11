import { BsCheck } from 'react-icons/bs';
import { useState } from 'react';
import { useSelector } from 'react-redux';
import { AiOutlineClose } from 'react-icons/ai';

import { ITodoCard } from '../../../../../../interfaces';
import ClickAway from '../../../../../Shared/ClickAway';
import { TRootState, useCreateCheckListMutation } from '../../../../../../state/store';

export interface ICardCheckListBtnProps {
  card: ITodoCard;
}

export interface IServerError {
  [key: string]: string;
}

const CardCheckListBtn = ({ card }: ICardCheckListBtnProps) => {
  const { token, user } = useSelector((store: TRootState) => store.user);
  const [createCheckListMut] = useCreateCheckListMutation();
  const [clickAwayOpen, setClickAwayOpen] = useState(false);
  const [checkList, setCheckList] = useState('');
  const [error, setError] = useState('');

  const handleOnClickAwayOpen = () => {
    setClickAwayOpen(true);
  };

  const handleOnClickAwayClose = () => {
    setClickAwayOpen(false);
  };

  const handleOnChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setCheckList(e.target.value);
  };

  const inputFieldError = (): boolean => {
    return !(checkList.trim().length > 0 && error.length === 0);
  };

  const applyServerError = <T extends IServerError>(data: T) => {
    for (let prop in data) {
      setError(data[prop]);
    }
  };

  const createCheckList = () => {
    const payload = {
      userId: user.id,
      title: checkList,
      todoCardId: card.id,
      token,
    };

    createCheckListMut(payload)
      .unwrap()
      .then(() => {
        clearForm();
      })
      .catch((err) => {
        applyServerError(err.data);
      });
  };

  const handleOnSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    setError('');

    if (inputFieldError()) {
      return;
    }
    createCheckList();
  };

  const clearForm = () => {
    setCheckList('');
    handleOnClickAwayClose();
  };

  return (
    <div className="p-1 bg-gray-800 rounded w-32 my-2 hover:bg-gray-700">
      <button onClick={handleOnClickAwayOpen} className="flex items-center text-sm">
        <BsCheck className="mr-2" />
        Checklist
      </button>
      {clickAwayOpen && (
        <ClickAway onClickAway={handleOnClickAwayClose}>
          <div className="bg-gray-700 rounded p-2 absolute z-10 right-20 w-[180px] md:w-[225px]">
            <div className="flex justify-between items-center">
              <div>&nbsp;</div>
              <h3>Add Checklist</h3>
              <div>
                <AiOutlineClose className="cursor-pointer" />
              </div>
            </div>
            <div>
              <form onSubmit={handleOnSubmit}>
                {error.length > 0 && (
                  <div className="my-1">
                    <p className="text-xs text-red-300">{error}</p>
                  </div>
                )}
                <div className="my-2 flex flex-col mb-1">
                  <label htmlFor="checklist" className="font-bold text-xs">
                    Title
                  </label>
                  <input
                    onChange={handleOnChange}
                    value={checkList}
                    type="text"
                    id="checklist"
                    name="checklist"
                    className="border-gray-400 border rounded h-9 bg-transparent"
                  />
                </div>
                <div className="my-2 flex justify-between">
                  <button type="submit" className="btn mx-1">
                    Add
                  </button>
                  <button
                    type="button"
                    onClick={handleOnClickAwayClose}
                    className="outline-btn rounded mx-1 !text-gray-400"
                  >
                    Cancel
                  </button>
                </div>
              </form>
            </div>
          </div>
        </ClickAway>
      )}
    </div>
  );
};

export default CardCheckListBtn;
