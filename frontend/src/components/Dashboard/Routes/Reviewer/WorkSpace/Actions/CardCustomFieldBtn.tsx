import { useState } from 'react';
import ClickAway from '../../../../../Shared/ClickAway';
import CardStartCustomField from '../CustomField/CardStartCustomField';
import CardCreateCustomField from '../CustomField/CardCreateCustomField';
import { ICustomFieldType, ICustomFieldTypeOption, ITodoCard } from '../../../../../../interfaces';
import CardCustomField from '../CustomField/CardCustomField';
import { TCustomFieldValue } from '../../../../../../types';
import { BiCustomize } from 'react-icons/bi';

export interface ICardCustomFieldBtnProps {
  card: ITodoCard;
}

const customFieldTypeState = {
  fieldName: '',
  fieldType: '',
  selectedTitle: '',
};

function CardCustomFieldBtn({ card }: ICardCustomFieldBtnProps) {
  const MAX_PAGES = 4;
  const [isClickAwayOpen, setIsClickAwayOpen] = useState(false);
  const [currentPage, setCurrentPage] = useState(1);
  const [customFieldType, setCustomFieldType] = useState<ICustomFieldType>(customFieldTypeState);

  const resetState = () => {
    setCurrentPage(1);
    setCustomFieldType({ fieldName: '', fieldType: '', selectedTitle: '' });
  };

  const navigateNextPage = (): void => {
    if (currentPage < MAX_PAGES) {
      setCurrentPage((prevState) => prevState + 1);
    }
  };

  const navigatePrevPage = (): void => {
    if (currentPage > 1) {
      setCurrentPage((prevState) => prevState - 1);
    }
  };

  const handleCloseClickAway = (): void => {
    resetState();
    setIsClickAwayOpen(false);
  };

  const handleOpenClickAway = (): void => setIsClickAwayOpen(true);

  const selectCustomField = (selectedTitle: string, fieldType: string): void => {
    const fieldName = fieldType.slice(0, 1) + fieldType.slice(1).toLowerCase();
    if (fieldType === 'DROPDOWN' || fieldType === 'CHECKBOX') {
      setCustomFieldType({ ...customFieldType, selectedTitle, fieldType, fieldName, options: [] });
    } else {
      setCustomFieldType({ ...customFieldType, selectedTitle, fieldType, fieldName, selectedValue: '' });
    }
  };

  const isOptionValue = (value: TCustomFieldValue): value is ICustomFieldTypeOption => {
    return (value as ICustomFieldTypeOption).id !== undefined;
  };

  const addCustomFieldValue = (customValue: TCustomFieldValue, fieldType: string): void => {
    if (fieldType === 'TEXT' || fieldType === 'NUMBER') {
      setCustomFieldType({ ...customFieldType, selectedValue: customValue });
    } else if (
      (fieldType === 'CHECKBOX' && isOptionValue(customValue)) ||
      (fieldType === 'DROPDOWN' && isOptionValue(customValue))
    ) {
      const { id, value } = customValue;
      setCustomFieldType({
        ...customFieldType,
        options: [...(customFieldType.options || []), { id, value }],
      });
    }
  };

  const deleteOption = (id: string) => {
    setCustomFieldType({
      ...customFieldType,
      options: customFieldType.options?.filter((option) => option.id !== id),
    });
  };

  const renderCurrentPage = (): React.ReactNode => {
    switch (currentPage) {
      case 1:
        return (
          <CardStartCustomField
            page={1}
            handleCloseClickAway={handleCloseClickAway}
            navigatePrevPage={navigatePrevPage}
            navigateNextPage={navigateNextPage}
          />
        );
      case 2:
        return (
          <CardCreateCustomField
            page={2}
            handleCloseClickAway={handleCloseClickAway}
            navigatePrevPage={navigatePrevPage}
            navigateNextPage={navigateNextPage}
            selectCustomField={selectCustomField}
          />
        );
      case 3:
        return (
          <CardCustomField
            page={3}
            customFieldType={customFieldType}
            deleteOption={deleteOption}
            navigatePrevPage={navigatePrevPage}
            handleCloseClickAway={handleCloseClickAway}
            addCustomFieldValue={addCustomFieldValue}
          />
        );
      default:
        return <></>;
    }
  };

  return (
    <>
      <div className="p-1 bg-gray-800 rounded w-32 my-2 hover:bg-gray-700 relative">
        <button onClick={handleOpenClickAway} className="flex items-center text-sm">
          <BiCustomize className="mr-2" />
          Custom Fields
        </button>
        {isClickAwayOpen && (
          <ClickAway onClickAway={handleCloseClickAway}>
            <div className="p-2 md:min-h-[400px] rounded bg-gray-700 absolute z-20 right-0 md:w-[325px] w-[90%] top-0">
              {renderCurrentPage()}
            </div>
          </ClickAway>
        )}
      </div>
    </>
  );
}

export default CardCustomFieldBtn;
