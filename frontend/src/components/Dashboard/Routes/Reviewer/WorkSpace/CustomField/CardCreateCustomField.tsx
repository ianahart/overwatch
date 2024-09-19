import { useState } from 'react';
import CardHeaderCustomField from './CardHeaderCustomField';

export interface ICardCreateCustomFieldProps {
  page: number;
  navigateNextPage: () => void;
  navigatePrevPage: () => void;
  handleCloseClickAway: () => void;
  selectCustomField: (selectedTitle: string, selectedType: string) => void;
}

const CardCreateCustomField = ({
  page,
  navigatePrevPage,
  navigateNextPage,
  handleCloseClickAway,
  selectCustomField,
}: ICardCreateCustomFieldProps) => {
  const [selectedTitle, setSelectedTitle] = useState('');
  const [selectedType, setSelectedType] = useState('');
  const [error, setError] = useState('');

  const fieldTypes = [
    { id: 1, fieldName: 'Checkbox', fieldType: 'CHECKBOX' },
    { id: 2, fieldName: 'Text', fieldType: 'TEXT' },
    { id: 3, fieldName: 'Dropdown', fieldType: 'DROPDOWN' },
    { id: 4, fieldName: 'Number', fieldType: 'NUMBER' },
  ];

  const handleOnSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    setError('');
    if (selectedTitle.trim().length === 0 || selectedType.trim().length === 0) {
      setError('Please make sure to include both fields');
      return;
    }
    selectCustomField(selectedTitle, selectedType);
    navigateNextPage();
  };

  return (
    <div className="min-h-[400px]">
      <CardHeaderCustomField
        page={page}
        handleCloseClickAway={handleCloseClickAway}
        navigatePrevPage={navigatePrevPage}
      />
      <form onSubmit={handleOnSubmit} className="p-2 min-h-[400px] flex flex-col justify-between">
        {error.length > 0 && (
          <div className="my-2">
            <p className="text-xs text-red-300">{error}</p>
          </div>
        )}
        <div className="flex flex-col items-start my-4">
          <label className="text-xs font-bold mb-1" htmlFor="title">
            Title
          </label>
          <input
            onChange={(e) => setSelectedTitle(e.target.value)}
            value={selectedTitle}
            className="w-full h-9 rounded bg-transparent border-gray-500 border"
            name="title"
            id="title"
            type="text"
            placeholder="Add a title..."
          />
        </div>
        <div className="flex flex-col items-start my-4">
          <label className="text-xs font-bold mb-1" htmlFor="type">
            Type
          </label>
          <select
            onChange={(e) => setSelectedType(e.target.value)}
            value={selectedType}
            className="bg-transparent w-full rounded h-9 border-gray-500 border"
            id="type"
            name="type"
          >
            <option hidden>Select</option>
            {fieldTypes.map((fieldType) => {
              return (
                <option key={fieldType.id} value={fieldType.fieldType}>
                  {fieldType.fieldName}
                </option>
              );
            })}
          </select>
        </div>
        <div className="mt-auto flex justify-between">
          <button
            type="submit"
            className="flex items-center justify-center hover:bg-gray-950 py-1 px-2 rounded bg-gray-900"
          >
            Next
          </button>
          <button type="button" onClick={handleCloseClickAway}>
            Cancel
          </button>
        </div>
      </form>
    </div>
  );
};

export default CardCreateCustomField;
