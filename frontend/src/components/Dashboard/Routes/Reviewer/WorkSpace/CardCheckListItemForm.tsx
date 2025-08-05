import { useState } from 'react';
import { useSelector } from 'react-redux';

import ClickAway from '../../../../Shared/ClickAway';
import { TRootState, useCreateCheckListItemMutation } from '../../../../../state/store';
import { ICheckListItem } from '../../../../../interfaces';

export interface ICardCheckListItemFormProps {
  closeForm: () => void;
  checkListId: number;
  addCheckListItem: (checkListItem: ICheckListItem) => void;
}

export interface IServerError {
  [key: string]: string;
}

const CardCheckListItemForm = ({ addCheckListItem, closeForm, checkListId }: ICardCheckListItemFormProps) => {
  const { token, user } = useSelector((store: TRootState) => store.user);
  const [createCheckListItemMut] = useCreateCheckListItemMutation();
  const [title, setTitle] = useState('');
  const [error, setError] = useState('');

  const validateForm = () => {
    return !(title.trim().length === 0 || title.length > 50 || error.length > 0);
  };

  const applyServerError = <T extends IServerError>(data: T) => {
    for (let prop in data) {
      setError(data[prop]);
    }
  };

  const handleOnSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    setError('');

    if (!validateForm()) {
      return;
    }
    const payload = { userId: user.id, token, checkListId, title };

    createCheckListItemMut(payload)
      .unwrap()
      .then((res) => {
        addCheckListItem(res.data);
        closeForm();
      })
      .catch((err) => {
        applyServerError(err.data);
      });
  };

  return (
    <ClickAway onClickAway={closeForm}>
      <form data-testid="CardCheckListItemForm" onSubmit={handleOnSubmit}>
        <div className="flex flex-col my-1">
          <label htmlFor={`checklistitem-${checkListId}`}>New Item</label>
          {error.length > 0 && <p className="text-xs text-red-300 my-1">{error}</p>}
          <input
            onChange={(e) => setTitle(e.target.value)}
            value={title}
            id={`checklistitem-${checkListId}`}
            name={`checklistitem-${checkListId}`}
            className="md:w-[50%] w-full h-9 rounded bg-transparent border border-gray-800"
          />
          <div className="my-2 md:w-[50%] w-full flex justify-between">
            <button type="submit" className="btn">
              Add
            </button>
            <button onClick={closeForm} type="button" className="outline-btn !text-gray-400">
              Cancel
            </button>
          </div>
        </div>
      </form>
    </ClickAway>
  );
};

export default CardCheckListItemForm;
