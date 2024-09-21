import { BsChevronDown, BsChevronUp } from 'react-icons/bs';

import { ICustomField } from '../../../../../interfaces';
import { useState } from 'react';

export interface ICardCustomFieldItemProps {
  customField: ICustomField;
}

const CardCustomFieldItem = ({ customField }: ICardCustomFieldItemProps) => {
  const [isValueOpen, setIsValueOpen] = useState(false);
  return (
    <div className="my-4 w-full md:w-[50%]">
      <div className="my-2 flex items-center justify-between">
        <p>{customField.fieldName}</p>
        <div className="cursor-pointer" onClick={() => setIsValueOpen((prevState) => !prevState)}>
          {isValueOpen ? <BsChevronUp /> : <BsChevronDown />}
        </div>
      </div>
      {isValueOpen && (
        <div
          className="my-2 ml-2 bg-gray-800 p-2 rounded
                    "
        >
          <p>{customField.selectedValue}</p>
          {customField.dropDownOptions?.map((dropDownOption) => {
            return (
              <div key={dropDownOption.id} className="flex items-center">
                {customField.fieldType === 'CHECKBOX' && <input className="mx-2" type="checkbox" />}
                <p>{dropDownOption.optionValue}</p>
              </div>
            );
          })}
        </div>
      )}
    </div>
  );
};

export default CardCustomFieldItem;
