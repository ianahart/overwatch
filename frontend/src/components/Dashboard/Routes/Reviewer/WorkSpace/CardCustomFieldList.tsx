import { useSelector } from 'react-redux';
import { useEffect, useState } from 'react';

import { TRootState, useFetchCustomFieldsQuery } from '../../../../../state/store';
import { ICustomField, ITodoCard } from '../../../../../interfaces';
import { BiCustomize } from 'react-icons/bi';
import CardCustomFieldItem from './CardCustomFieldItem';
import Spinner from '../../../../Shared/Spinner';

export interface ICardCustomFieldListProps {
  card: ITodoCard;
}

const CardCustomFieldList = ({ card }: ICardCustomFieldListProps) => {
  const { token } = useSelector((store: TRootState) => store.user);
  const isActive = 'true';
  const { data, isLoading } = useFetchCustomFieldsQuery({ token, todoCardId: card.id, isActive });
  const [customFields, setCustomFields] = useState<ICustomField[]>([]);

  useEffect(() => {
    if (data !== undefined) {
      setCustomFields(data.data);
    }
  }, [data]);

  return (
    <div className="my-8">
      <div className="flex items-center">
        <BiCustomize className="mr-1 text-xl" />
        <div>
          <p>Custom Fields</p>
        </div>
      </div>
      {isLoading && (
        <div className="my-2 justify-center flex">
          <Spinner message="Loading custom fields..." />
        </div>
      )}
      <div className="my-8">
        {customFields.map((customField) => {
          return <CardCustomFieldItem key={customField.id} customField={customField} />;
        })}
      </div>
    </div>
  );
};

export default CardCustomFieldList;
