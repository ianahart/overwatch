import { useState } from 'react';
import { TCustomFieldValue } from '../../../../../../types';

export interface INumberTextInputFieldProps {
  fieldType: string;
  selectedTitle: string;
  handleCloseClickAway: () => void;
  addCustomFieldValue: (value: TCustomFieldValue, fieldType: string) => void;
}

const NumberTextInputField = ({
  fieldType,
  selectedTitle,
  handleCloseClickAway,
  addCustomFieldValue,
}: INumberTextInputFieldProps) => {
  const [inputValue, setInputValue] = useState('');
  const placeholder = fieldType === 'TEXT' ? 'Text' : 'Number';
  const attr = fieldType === 'TEXT' ? 'text' : 'number';

  const handleOnAddValue = () => {
    addCustomFieldValue(inputValue, fieldType);
  };

  const handleOnSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    if (inputValue.trim().length === 0) return;

    console.log('NumberTextInputField.tsx submit () =>');
    setInputValue('');
  };

  return (
    <form onSubmit={handleOnSubmit} className="my-8 flex flex-col justify-between h-full min-h-[350px]">
      <div className="mb-4">
        <h3 className="text-xl">{selectedTitle}</h3>
      </div>
      <div>
        <div className="flex justify-between">
          <input
            placeholder={`Add ${placeholder}...`}
            onChange={(e) => setInputValue(e.target.value)}
            value={inputValue}
            className="w-[65%] h-9 rounded bg-transparent border border-gray-500"
            id={attr}
            name={attr}
            type={attr}
          />
          <div className="w-[30%]">
            <button
              onClick={handleOnAddValue}
              className="flex items-center justify-center hover:bg-gray-950 py-1 px-2 rounded bg-gray-900 w-full h-9"
              type="button"
            >
              Create
            </button>
          </div>
        </div>
      </div>

      <div className="mt-auto flex justify-between">
        <button
          type="submit"
          className="flex w-full items-center justify-center hover:bg-gray-950 py-1 px-2 rounded bg-gray-900"
        >
          Save
        </button>
        <button className="w-full" type="button" onClick={handleCloseClickAway}>
          Cancel
        </button>
      </div>
    </form>
  );
};

export default NumberTextInputField;
