import { TbNumber } from 'react-icons/tb';
import { RxDropdownMenu } from 'react-icons/rx';
import { IoIosCheckboxOutline } from 'react-icons/io';
import { RiText } from 'react-icons/ri';
import { BsChevronDown, BsChevronUp, BsTrash } from 'react-icons/bs';
import { MdOutlineQuestionMark } from 'react-icons/md';
import { ICustomField } from '../../../../../../interfaces';
import { useState } from 'react';

export interface ICustomFieldItemProps {
  customField: ICustomField;
  deleteCustomField: (id: number) => void;
  deleteDropDownOption: (id: number, customFieldId: number) => void;
}

const CustomFieldItem = ({ customField, deleteCustomField, deleteDropDownOption }: ICustomFieldItemProps) => {
  const [isValueOpen, setIsValueOpen] = useState(false);

  const renderFieldIcon = (fieldType: string): JSX.Element => {
    switch (fieldType) {
      case 'CHECKBOX':
        return <IoIosCheckboxOutline />;
      case 'NUMBER':
        return <TbNumber />;
      case 'TEXT':
        return <RiText />;
      case 'DROPDOWN':
        return <RxDropdownMenu />;
      default:
        return <MdOutlineQuestionMark />;
    }
  };

  const handleOnDeleteCustomField = (): void => {
    deleteCustomField(customField.id);
  };

  const handleOnDeleteDropDownOption = (id: number): void => {
    deleteDropDownOption(id, customField.id);
  };

  return (
    <div key={customField.id} className="my-4 border border-gray-600 rounded p-1">
      <div className={`flex ${!isValueOpen ? 'justify-between' : 'justify-normal'}`}>
        <div className="flex-[3]">
          <div className="flex items-center">
            <div className="mr-2">{renderFieldIcon(customField.fieldType)}</div>
            <div>{customField.fieldName}</div>
          </div>
          {isValueOpen && (
            <>
              {customField.selectedValue.length > 0 ? (
                <div>{customField.selectedValue}</div>
              ) : (
                <div className="bg-gray-800 rounded">
                  {customField.dropDownOptions.map((dropDownOption) => {
                    return (
                      <div
                        key={dropDownOption.id}
                        className="flex items-center my-2 border-t border-gray-700 py-1 px-2 justify-between"
                      >
                        <p>{dropDownOption.optionValue}</p>
                        <div
                          onClick={() => handleOnDeleteDropDownOption(dropDownOption.id)}
                          className="cursor-pointer ml-4"
                        >
                          <BsTrash />
                        </div>
                      </div>
                    );
                  })}
                </div>
              )}
            </>
          )}
        </div>
        <div className="flex items-center justify-end flex-[1]">
          <div onClick={() => setIsValueOpen((prevState) => !prevState)} className="mr-2 cursor-pointer">
            {isValueOpen ? <BsChevronUp /> : <BsChevronDown />}
          </div>
          {!isValueOpen && (
            <div onClick={handleOnDeleteCustomField} className="cursor-pointer">
              <BsTrash />
            </div>
          )}
        </div>
      </div>
    </div>
  );
};
export default CustomFieldItem;
