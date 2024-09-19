import { useState } from 'react';
import { nanoid } from 'nanoid';
import { BsTrash } from 'react-icons/bs';
import { useSelector } from 'react-redux';

import { TCustomFieldValue } from '../../../../../../types';
import { ICustomFieldType, ICustomFieldTypeOption, IError } from '../../../../../../interfaces';
import { TRootState, useCreateCustomFieldMutation } from '../../../../../../state/store';

export interface ICheckBoxInputFieldProps {
  todoCardId: number;
  customFieldType: ICustomFieldType;
  options: ICustomFieldTypeOption[];
  handleCloseClickAway: () => void;
  addCustomFieldValue: (value: TCustomFieldValue, fieldType: string) => void;
  deleteOption: (id: string) => void;
}

const CheckBoxInputField = ({
  todoCardId,
  customFieldType,
  options,
  handleCloseClickAway,
  addCustomFieldValue,
  deleteOption,
}: ICheckBoxInputFieldProps) => {
  const { user, token } = useSelector((store: TRootState) => store.user);
  const [createCustomFieldMut] = useCreateCustomFieldMutation();
  const MAX_CHECKBOXES = 10;
  const [error, setError] = useState('');
  const [inputValue, setInputValue] = useState('');

  const handleOnAddCheckBox = (e: React.MouseEvent<HTMLButtonElement>): void => {
    e.stopPropagation();
    setError('');
    const newCheckBox = { id: nanoid(), value: inputValue };
    if (options.length < MAX_CHECKBOXES) {
      addCustomFieldValue(newCheckBox, customFieldType.fieldType);
    } else {
      setError(`You have added the maximum amount of checkboxes (${MAX_CHECKBOXES})`);
    }
    setInputValue('');
  };

  const applyServerError = (res: IError): void => {
    for (let prop in res) {
      setError(res[prop]);
    }
  };

  const handleOnSubmit = (e: React.FormEvent<HTMLFormElement>): void => {
    e.preventDefault();
    setError('');
    if (options.length === 0) return;

    const payload = {
      token,
      todoCardId,
      userId: user.id,
      fieldType: customFieldType.fieldType,
      fieldName: customFieldType.selectedTitle,
      selectedValue: '',
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

    console.log('CheckBoxInputField.tsx submit () =>');
  };

  return (
    <form onSubmit={handleOnSubmit} className="my-8 flex flex-col justify-between  h-[350px] overflow-y-auto">
      {error.length > 0 && (
        <div>
          <p className="text-xs text-red-300">{error}</p>
        </div>
      )}
      <div className="mb-4">
        <h3 className="text-xl">{customFieldType.selectedTitle}</h3>
      </div>
      <div>
        <div className="flex justify-between">
          <input
            onChange={(e) => setInputValue(e.target.value)}
            value={inputValue}
            placeholder="Checkbox label..."
            className="w-[65%] h-9 rounded bg-transparent border border-gray-500"
            id="label"
            name="label"
            type="text"
          />
          <div className="w-[30%]">
            <button
              onClick={handleOnAddCheckBox}
              className="flex items-center justify-center hover:bg-gray-950 py-1 px-2 rounded bg-gray-900 w-full h-9"
              type="button"
            >
              Create
            </button>
          </div>
        </div>
        {options.length > 0 && (
          <div className="flex flex-col items-start my-2 bg-gray-600 rounded p-2">
            {options.map((option) => {
              return (
                <div key={option.id} className="flex justify-between w-full my-2">
                  <div className="flex items-center">
                    <input className="mr-2" type="checkbox" disabled checked />
                    <p>{option.value}</p>
                  </div>
                  <div className="cursor-pointer" onClick={() => deleteOption(option.id)}>
                    <BsTrash />
                  </div>
                </div>
              );
            })}
          </div>
        )}
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

export default CheckBoxInputField;
