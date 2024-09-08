import { BsChevronLeft } from 'react-icons/bs';
import { useSelector } from 'react-redux';
import { useState } from 'react';

import { ITodoCard } from '../../../../../../interfaces';
import { labelColors } from '../../../../../../data';
import { TRootState } from '../../../../../../state/store';

export interface ICardLabelFormProps {
  handleOnCloseLabelForm: () => void;
  card: ITodoCard;
}

const CardLabelForm = ({ handleOnCloseLabelForm, card }: ICardLabelFormProps) => {
  const { workSpace } = useSelector((store: TRootState) => store.workSpace);
  const MAX_LABEL_SIZE = 20;
  const [color, setColor] = useState('');
  const [label, setLabel] = useState('');

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

  const handleOnSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    if (color.length === 0 || label.trim().length === 0) {
      return;
    }

    console.log(`Saving label-${label} with background color: ${color}`, workSpace.id);
  };

  return (
    <div>
      <BsChevronLeft className="cursor-pointer" onClick={handleOnCloseLabelForm} />
      <div className="p-2">
        <div
          style={{ background: color ? color : '#333' }}
          className="p-2 w-full h-9 rounded flex flex-col justify-center"
        >
          <p className="break-all text-white font-bold">{label}</p>
        </div>
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
