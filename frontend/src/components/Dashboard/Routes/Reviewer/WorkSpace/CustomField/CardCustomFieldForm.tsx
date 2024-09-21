import { TCustomFieldValue } from '../../../../../../types';
import { ICustomFieldType } from '../../../../../../interfaces';
import CardHeaderCustomField from './CardHeaderCustomField';
import CheckBoxInputField from './CheckBoxInputField';
import DropDownInputField from './DropDownInputField';
import NumberTextInputField from './NumberTextInputField';

export interface ICardCustomFieldFormProps {
  todoCardId: number;
  customFieldType: ICustomFieldType;
  page: number;
  navigatePrevPage: () => void;
  handleCloseClickAway: () => void;
  addCustomFieldValue: (value: TCustomFieldValue, fieldType: string) => void;
  deleteOption: (id: string) => void;
}

const CardCustomFieldForm = ({
  todoCardId,
  customFieldType,
  page,
  navigatePrevPage,
  handleCloseClickAway,
  addCustomFieldValue,
  deleteOption,
}: ICardCustomFieldFormProps) => {
  const renderCustomField = (): JSX.Element => {
    switch (customFieldType.fieldType) {
      case 'NUMBER':
        return (
          <NumberTextInputField
            customFieldType={customFieldType}
            handleCloseClickAway={handleCloseClickAway}
            addCustomFieldValue={addCustomFieldValue}
            todoCardId={todoCardId}
          />
        );
      case 'TEXT':
        return (
          <NumberTextInputField
            customFieldType={customFieldType}
            handleCloseClickAway={handleCloseClickAway}
            addCustomFieldValue={addCustomFieldValue}
            todoCardId={todoCardId}
          />
        );
      case 'CHECKBOX':
        return (
          <CheckBoxInputField
            customFieldType={customFieldType}
            options={customFieldType.options || []}
            handleCloseClickAway={handleCloseClickAway}
            addCustomFieldValue={addCustomFieldValue}
            deleteOption={deleteOption}
            todoCardId={todoCardId}
          />
        );
      case 'DROPDOWN':
        return (
          <DropDownInputField
            customFieldType={customFieldType}
            options={customFieldType.options || []}
            handleCloseClickAway={handleCloseClickAway}
            addCustomFieldValue={addCustomFieldValue}
            deleteOption={deleteOption}
            todoCardId={todoCardId}
          />
        );
      default:
        return <></>;
    }
  };

  return (
    <>
      <CardHeaderCustomField
        navigatePrevPage={navigatePrevPage}
        page={page}
        handleCloseClickAway={handleCloseClickAway}
      />
      {renderCustomField()}
    </>
  );
};

export default CardCustomFieldForm;
