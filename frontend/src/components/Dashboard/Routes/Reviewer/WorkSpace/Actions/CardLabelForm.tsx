import { BsChevronLeft } from 'react-icons/bs';
import { useSelector } from 'react-redux';
import { useState } from 'react';

import { labelColors } from '../../../../../../data';
import { TRootState, useCreateLabelMutation } from '../../../../../../state/store';

export interface ICardLabelFormProps {
  handleOnCloseLabelForm: () => void;
}

interface IServerError {
  [key: string]: string;
}

const CardLabelForm = ({ handleOnCloseLabelForm }: ICardLabelFormProps) => {
  const { workSpace } = useSelector((store: TRootState) => store.workSpace);
  const { user, token } = useSelector((store: TRootState) => store.user);
  const [createLabelMut] = useCreateLabelMutation();
  const MAX_LABEL_SIZE = 20;
  const [color, setColor] = useState('');
  const [label, setLabel] = useState('');
  const [error, setError] = useState('');

  const handleOnChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const value = e.target.value;

    if (value.trim().length > MAX_LABEL_SIZE) {
      return;
    }
    setLabel(value);
  };

  const handleOnClearLabel = () => {
    setColor('');
    setLabel('');
  };

  const applyServerError = <T extends IServerError>(data: T) => {
    for (let prop in data) {
      setError(data[prop]);
    }
  };

  const handleOnSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    setError('');
    if (color.length === 0 || label.trim().length === 0) {
      return;
    }
    const payload = { userId: user.id, token, title: label, color, workSpaceId: workSpace.id };
    createLabelMut(payload)
      .unwrap()
      .then(() => {
        handleOnClearLabel();
        handleOnCloseLabelForm();
      })
      .catch((err) => {
        applyServerError(err.data);
      });
  };

  return (
    <div data-testid="CardLabelForm">
      <BsChevronLeft
        data-testid="card-label-form-close-icon"
        className="cursor-pointer"
        onClick={handleOnCloseLabelForm}
      />
      <div className="p-2">
        <div
          style={{ background: color ? color : '#333' }}
          className="p-2 w-full h-9 rounded flex flex-col justify-center"
        >
          <p className="break-all text-white font-bold">{label}</p>
        </div>
        {error.length > 0 && <p className="text-xs text-red-300 my-1">{error}</p>}
        <form onSubmit={handleOnSubmit} className="my-2">
          <div>
            <label className="text-xs font-bold" htmlFor="label">
              Title
            </label>
            <input
              value={label}
              onChange={handleOnChange}
              className="w-full h-9 bg-transparent rounded border border-gray-600"
              type="text"
              id="label"
              name="label"
            />
          </div>
          <div className="mt-1">
            <p className="text-xs font-bold">Background Color</p>
          </div>
          <div className="my-1 flex flex-wrap p-1 rounded">
            {labelColors.map(({ id, background }) => {
              return (
                <div
                  onClick={() => setColor(background)}
                  style={{ background }}
                  key={id}
                  className={`rounded h-6 w-6 m-1 cursor-pointer ${
                    color === background ? 'opacity-40 border-2 border-black' : 'opacity-100 border-none'
                  }`}
                ></div>
              );
            })}
            <div className="flex justify-center">
              <button
                type="button"
                onClick={handleOnClearLabel}
                className="bg-gray-800 text-xs rounded px-2 hover:opacity-70 flex items-center"
              >
                Clear
              </button>
            </div>
          </div>
          {color.length > 0 && label.trim().length > 0 && (
            <div className="flex justify-between">
              <button type="submit" className="btn !text-sm">
                Save
              </button>
              <button onClick={handleOnCloseLabelForm} type="button" className="outline-btn !text-white !text-sm">
                Cancel
              </button>
            </div>
          )}
        </form>
      </div>
    </div>
  );
};

export default CardLabelForm;
