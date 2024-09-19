import { useState } from 'react';
import { useSelector } from 'react-redux';

import { TCustomFieldValue } from '../../../../../../types';
import { ICustomFieldType, IError } from '../../../../../../interfaces';
import { TRootState, useCreateCustomFieldMutation } from '../../../../../../state/store';

export interface INumberTextInputFieldProps {
  todoCardId: number;
  customFieldType: ICustomFieldType;
  handleCloseClickAway: () => void;
  addCustomFieldValue: (value: TCustomFieldValue, fieldType: string) => void;
}

const NumberTextInputField = ({
  todoCardId,
  customFieldType,
  handleCloseClickAway,
  addCustomFieldValue,
}: INumberTextInputFieldProps) => {
  const { user, token } = useSelector((store: TRootState) => store.user);
  const [error, setError] = useState('');
  const [createCustomFieldMut] = useCreateCustomFieldMutation();
  const [inputValue, setInputValue] = useState('');
  const placeholder = customFieldType.fieldType === 'TEXT' ? 'Text' : 'Number';
  const attr = customFieldType.fieldType === 'TEXT' ? 'text' : 'number';

  const applyServerError = (res: IError): void => {
    for (let prop in res) {
      setError(res[prop]);
    }
  };

  const handleOnSubmit = (e: React.FormEvent<HTMLFormElement>): void => {
    setError('');
    e.preventDefault();
    if (inputValue.trim().length === 0 || inputValue.length > 50) {
      setError('Value must be under 50 characters');
      return;
    }
    addCustomFieldValue(inputValue, customFieldType.fieldType);

    const payload = {
      token,
      todoCardId,
      userId: user.id,
      fieldType: customFieldType.fieldType,
      fieldName: customFieldType.selectedTitle,
      selectedValue: inputValue,
      dropDownOptions: customFieldType.options || [],
    };

    createCustomFieldMut(payload)
      .unwrap()
      .then(() => {
        handleCloseClickAway();
      })
      .catch((err) => {
        applyServerError(err.data);
      });

    setInputValue('');
  };

  return (
    <form onSubmit={handleOnSubmit} className="my-8 flex flex-col justify-between h-full min-h-[350px]">
      <div className="mb-4">
        <h3 className="text-xl">{customFieldType.selectedTitle}</h3>
        {error.length > 0 && (
          <div className="my-1">
            <p className="text-xs text-red-300">{error}</p>
          </div>
        )}
      </div>
      <div>
        <div className="flex justify-between">
          <input
            placeholder={`Add ${placeholder}...`}
            onChange={(e) => setInputValue(e.target.value)}
            value={inputValue}
            className="w-full h-9 rounded bg-transparent border border-gray-500"
            id={attr}
            name={attr}
            type={attr}
          />
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
