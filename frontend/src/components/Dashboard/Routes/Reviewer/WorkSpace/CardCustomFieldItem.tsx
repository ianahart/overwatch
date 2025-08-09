import { BsChevronDown, BsChevronUp } from 'react-icons/bs';

import { ICustomField } from '../../../../../interfaces';
import { useState } from 'react';

export interface ICardCustomFieldItemProps {
  customField: ICustomField;
}

const CardCustomFieldItem = ({ customField }: ICardCustomFieldItemProps) => {
  const [isValueOpen, setIsValueOpen] = useState(false);
  return (
    <div data-testid="CardCustomFieldItem" className="my-4 w-full md:w-[50%]">
      <div className="my-2 flex items-center justify-between">
        <p>{customField.fieldName}</p>
        <div
          data-testid="card-custom-field-item-button"
          className="cursor-pointer"
          onClick={() => setIsValueOpen((prevState) => !prevState)}
        >
          {isValueOpen ? (
            <BsChevronUp data-testid="card-custom-field-item-chevron-up" />
          ) : (
            <BsChevronDown data-testid="card-custom-field-item-chevron-down" />
          )}
        </div>
      </div>
      {isValueOpen && (
        <div className="my-2 ml-2 bg-gray-800 p-2 rounded">
          <p>-{customField.selectedValue}</p>
          {customField.dropDownOptions?.map((dropDownOption) => {
            return (
              <div key={dropDownOption.id} className="flex items-center">
                {customField.fieldType === 'CHECKBOX' && <input className="mx-2" type="checkbox" />}
                <p data-testid={`card-custom-field-item-dropdown-option-${dropDownOption.optionValue}`}>
                  {dropDownOption.optionValue}
                </p>
              </div>
            );
          })}
        </div>
      )}
    </div>
  );
};

export default CardCustomFieldItem;
