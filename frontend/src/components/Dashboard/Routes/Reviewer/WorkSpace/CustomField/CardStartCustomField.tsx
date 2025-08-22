import { AiOutlinePlus } from 'react-icons/ai';
import { useSelector } from 'react-redux';

import CardHeaderCustomField from './CardHeaderCustomField';
import {
  TRootState,
  useDeleteCustomFieldMutation,
  useDeleteDropDownOptionMutation,
  useFetchCustomFieldsQuery,
  useUpdateCustomFieldMutation,
} from '../../../../../../state/store';
import { useEffect, useState } from 'react';
import { ICustomField } from '../../../../../../interfaces';
import Spinner from '../../../../../Shared/Spinner';
import CustomFieldList from './CustomFieldList';

export interface ICardStartCustomFieldProps {
  page: number;
  todoCardId: number;
  navigateNextPage: () => void;
  navigatePrevPage: () => void;
  handleCloseClickAway: () => void;
}

const CardStartCustomField = ({
  page,
  todoCardId,
  navigatePrevPage,
  navigateNextPage,
  handleCloseClickAway,
}: ICardStartCustomFieldProps) => {
  const { token } = useSelector((store: TRootState) => store.user);
  const isActive = 'false';
  const { data, isLoading } = useFetchCustomFieldsQuery(
    { token, todoCardId, isActive },
    { skip: !token || !todoCardId }
  );
  const [deleteDropDownOptionMut] = useDeleteDropDownOptionMutation();
  const [deleteCustomFieldMut] = useDeleteCustomFieldMutation();
  const [updateCustomFieldMut] = useUpdateCustomFieldMutation();
  const [customFields, setCustomFields] = useState<ICustomField[]>([]);

  useEffect(() => {
    if (data !== undefined) {
      setCustomFields(data.data);
    }
  }, [data]);

  const deleteCustomField = (id: number): void => {
    deleteCustomFieldMut({ token, id })
      .unwrap()
      .then(() => {})
      .catch((err) => {
        console.log(err);
      });
  };

  const deleteDropDownOption = (id: number, customFieldId: number): void => {
    deleteDropDownOptionMut({ token, id })
      .unwrap()
      .then(() => {
        const updatedCustomFields = customFields.map((customField) => {
          if (customField.id === customFieldId) {
            return {
              ...customField,
              dropDownOptions: [...customField.dropDownOptions].filter((dropDownOption) => dropDownOption.id !== id),
            };
          }
          return { ...customField };
        });
        setCustomFields(updatedCustomFields);
      })
      .catch((err) => {
        console.log(err);
      });
  };

  const updateCustomFieldActive = (id: number, isActive: boolean): void => {
    updateCustomFieldMut({ token, id, isActive })
      .unwrap()
      .then(() => {})
      .catch((err) => {
        console.log(err);
      });
  };

  return (
    <div className="min-h-[400px] flex flex-col">
      <CardHeaderCustomField
        page={page}
        handleCloseClickAway={handleCloseClickAway}
        navigatePrevPage={navigatePrevPage}
      />
      <div className="flex flex-col flex-grow justify-between my-6">
        <div className="overflow-y-auto h-[275px] my-2">
          {isLoading ? (
            <div className="flex justify-center">
              <Spinner message="Loading custom fields..." />
            </div>
          ) : (
            <div>
              <h3 className="font-bold">Your custom fields ({customFields.length})</h3>
              <CustomFieldList
                customFields={customFields}
                deleteCustomField={deleteCustomField}
                deleteDropDownOption={deleteDropDownOption}
                updateCustomFieldActive={updateCustomFieldActive}
              />
            </div>
          )}
        </div>
        <div>Suggested fields</div>
      </div>
      <div className="mt-auto">
        <button
          data-testid="new-field-btn"
          onClick={navigateNextPage}
          className="flex items-center justify-center hover:bg-gray-950 py-1 px-2 w-full rounded bg-gray-900"
        >
          <AiOutlinePlus className="mr-2" /> New field
        </button>
      </div>
    </div>
  );
};
export default CardStartCustomField;
