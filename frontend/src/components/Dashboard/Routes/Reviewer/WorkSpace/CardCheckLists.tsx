import { useSelector } from 'react-redux';
import { ICheckList, ITodoCard } from '../../../../../interfaces';
import { TRootState, useFetchCheckListsQuery } from '../../../../../state/store';
import { useEffect, useState } from 'react';
import Spinner from '../../../../Shared/Spinner';
import CardCheckList from './CardCheckList';

export interface ICardCheckListsProps {
  card: ITodoCard;
}

const CardCheckLists = ({ card }: ICardCheckListsProps) => {
  const { token } = useSelector((store: TRootState) => store.user);
  const { data, isLoading } = useFetchCheckListsQuery({ token, todoCardId: card.id });
  const [checkLists, setCheckLists] = useState<ICheckList[]>([]);

  useEffect(() => {
    if (data !== undefined) {
      setCheckLists(data.data);
    }
  }, [data]);

  return (
    <div>
      {isLoading && (
        <div className="my-2">
          <Spinner message="Loading Checklists..." />
        </div>
      )}
      {!isLoading && (
        <>
          <h3 className="font-bold">Checklists</h3>
          <div className="my-2 max-w-[500px] w-full">
            {checkLists.map((checkList) => {
              return <CardCheckList key={checkList.id} checkList={checkList} />;
            })}
          </div>
        </>
      )}
    </div>
  );
};

export default CardCheckLists;
