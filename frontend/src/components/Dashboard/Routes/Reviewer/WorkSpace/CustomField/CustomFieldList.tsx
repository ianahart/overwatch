import { ICustomField } from '../../../../../../interfaces';
import CustomFieldItem from './CustomFieldItem';

export interface ICustomFieldListProps {
  customFields: ICustomField[];
  deleteCustomField: (id: number) => void;
  deleteDropDownOption: (id: number, customFieldId: number) => void;
  updateCustomFieldActive: (id: number, isActive: boolean) => void;
}

const CustomFieldList = ({
  customFields,
  deleteCustomField,
  deleteDropDownOption,
  updateCustomFieldActive,
}: ICustomFieldListProps) => {
  return (
    <div>
      {customFields.map((customField) => {
        return (
          <CustomFieldItem
            key={customField.id}
            customField={customField}
            deleteCustomField={deleteCustomField}
            deleteDropDownOption={deleteDropDownOption}
            updateCustomFieldActive={updateCustomFieldActive}
          />
        );
      })}
    </div>
  );
};

export default CustomFieldList;
