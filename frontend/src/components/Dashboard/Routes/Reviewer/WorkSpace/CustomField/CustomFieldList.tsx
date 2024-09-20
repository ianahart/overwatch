import { ICustomField } from '../../../../../../interfaces';
import CustomFieldItem from './CustomFieldItem';

export interface ICustomFieldListProps {
  customFields: ICustomField[];
  deleteCustomField: (id: number) => void;
  deleteDropDownOption: (id: number, customFieldId: number) => void;
}

const CustomFieldList = ({ customFields, deleteCustomField, deleteDropDownOption }: ICustomFieldListProps) => {
  return (
    <div>
      {customFields.map((customField) => {
        return (
          <CustomFieldItem
            key={customField.id}
            customField={customField}
            deleteCustomField={deleteCustomField}
            deleteDropDownOption={deleteDropDownOption}
          />
        );
      })}
    </div>
  );
};

export default CustomFieldList;
