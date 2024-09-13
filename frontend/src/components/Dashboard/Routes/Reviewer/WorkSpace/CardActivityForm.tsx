import { useSelector } from 'react-redux';
import { useState } from 'react';

import Avatar from '../../../../Shared/Avatar';
import { TRootState, useCreateActivityMutation } from '../../../../../state/store';

export interface IServerError {
  [key: string]: string;
}

export interface ICardActivityFormProps {
  todoCardId: number;
}

const CardActivityForm = ({ todoCardId }: ICardActivityFormProps) => {
  const MAX_CHARACTERS = 200;
  const { token, user } = useSelector((store: TRootState) => store.user);
  const [createActivityMut] = useCreateActivityMutation();
  const [inputValue, setInputValue] = useState('');
  const [error, setError] = useState('');

  const handleOnChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setInputValue(e.target.value);
  };

  const applyServerError = <T extends IServerError>(data: T) => {
    for (let prop in data) {
      setError(data[prop]);
    }
  };

  const validateForm = () => {
    return !(inputValue.trim().length === 0 || inputValue.length > MAX_CHARACTERS || error.length > 0);
  };

  const handleOnSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    setError('');
    if (!validateForm()) {
      return;
    }
    const payload = {
      token,
      userId: user.id,
      text: inputValue,
      todoCardId,
    };
    createActivityMut(payload)
      .unwrap()
      .then(() => {
        setInputValue('');
      })
      .catch((err) => {
        applyServerError(err.data);
      });
  };

  return (
    <form onSubmit={handleOnSubmit}>
      {error.length > 0 && (
        <div className="my-2">
          <p className="text-xs text-red-300">{error}</p>
        </div>
      )}
      <div className="flex items-center">
        <div className="mr-2">
          <Avatar width="w-9" height="h-9" initials={user.abbreviation} avatarUrl={user.avatarUrl} />
        </div>
        <input
          onChange={handleOnChange}
          value={inputValue}
          className="bg-gray-800 rounded-3xl p-1 h-9 w-full md:w-[70%]"
          placeholder="Write a comment..."
        />
      </div>
      {inputValue.trim().length > 0 && (
        <div className="flex items-center my-8">
          <button type="submit" className="btn mx-2">
            Create
          </button>
          <button type="button" onClick={() => setInputValue('')} className="outline-btn mx-2 !text-gray-400">
            Cancel
          </button>
        </div>
      )}
    </form>
  );
};

export default CardActivityForm;
