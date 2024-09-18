import { TCustomFieldValue } from '../../../../../../types';
import { ICustomFieldType } from '../../../../../../interfaces';
import CardHeaderCustomField from './CardHeaderCustomField';
import CheckBoxInputField from './CheckBoxInputField';
import DropDownInputField from './DropDownInputField';
import NumberTextInputField from './NumberTextInputField';

export interface ICardCustomFieldProps {
  customFieldType: ICustomFieldType;
  page: number;
  navigatePrevPage: () => void;
  handleCloseClickAway: () => void;
  addCustomFieldValue: (value: TCustomFieldValue, fieldType: string) => void;
  deleteOption: (id: string) => void;
}

const CardCustomField = ({
  customFieldType,
  page,
  navigatePrevPage,
  handleCloseClickAway,
  addCustomFieldValue,
  deleteOption,
}: ICardCustomFieldProps) => {
  const renderCustomField = (): JSX.Element => {
    switch (customFieldType.fieldType) {
      case 'NUMBER':
        return (
          <NumberTextInputField
            selectedTitle={customFieldType.selectedTitle}
            fieldType={customFieldType.fieldType}
            handleCloseClickAway={handleCloseClickAway}
            addCustomFieldValue={addCustomFieldValue}
          />
        );
      case 'TEXT':
        return (
          <NumberTextInputField
            selectedTitle={customFieldType.selectedTitle}
            fieldType={customFieldType.fieldType}
            handleCloseClickAway={handleCloseClickAway}
            addCustomFieldValue={addCustomFieldValue}
          />
        );
      case 'CHECKBOX':
        return (
          <CheckBoxInputField
            selectedTitle={customFieldType.selectedTitle}
            fieldType={customFieldType.fieldType}
            options={customFieldType.options || []}
            handleCloseClickAway={handleCloseClickAway}
            addCustomFieldValue={addCustomFieldValue}
            deleteOption={deleteOption}
          />
        );
      case 'DROPDOWN':
        return (
          <DropDownInputField
            selectedTitle={customFieldType.selectedTitle}
            fieldType={customFieldType.fieldType}
            options={customFieldType.options || []}
            handleCloseClickAway={handleCloseClickAway}
            addCustomFieldValue={addCustomFieldValue}
            deleteOption={deleteOption}
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

export default CardCustomField;
