import { useSelector } from 'react-redux';
import { ICheckList, ICheckListItem, ITodoCard } from '../../../../../interfaces';
import {
  TRootState,
  useDeleteCheckListItemMutation,
  useFetchCheckListsQuery,
  useUpdateCheckListItemMutation,
} from '../../../../../state/store';
import { useEffect, useState } from 'react';
import Spinner from '../../../../Shared/Spinner';
import CardCheckList from './CardCheckList';
import { BsCardChecklist } from 'react-icons/bs';

export interface ICardCheckListsProps {
  card: ITodoCard;
}

const CardCheckLists = ({ card }: ICardCheckListsProps) => {
  const { token } = useSelector((store: TRootState) => store.user);
  const { data, isLoading } = useFetchCheckListsQuery({ token, todoCardId: card.id }, { skip: !token || !card.id });
  const [updateCheckListItemMut] = useUpdateCheckListItemMutation();
  const [deleteCheckListItemMut] = useDeleteCheckListItemMutation();
  const [checkLists, setCheckLists] = useState<ICheckList[]>([]);

  useEffect(() => {
    if (data !== undefined) {
      setCheckLists(data.data);
    }
  }, [data]);

  const deleteCheckListItem = (checkListItem: ICheckListItem) => {
    deleteCheckListItemMut({ token, id: checkListItem.id })
      .unwrap()
      .then(() => {
        deleteCheckListItemState(checkListItem);
      });
  };

  const deleteCheckListItemState = (checkListItem: ICheckListItem) => {
    const filteredCheckLists = checkLists.map((checkList) => {
      if (checkList.id === checkListItem.checkListId) {
        return { ...checkList, checkListItems: checkList.checkListItems.filter((cli) => cli.id !== checkListItem.id) };
      }
      return { ...checkList };
    });

    setCheckLists(filteredCheckLists);
  };

  const updateCheckListItem = (checkListItem: ICheckListItem, key: string) => {
    updateCheckListItemMut({ token, data: checkListItem })
      .unwrap()
      .then((res) => {
        console.log(res);
      })
      .catch((err) => {
        console.log(err);
      });
    updateCheckListItemState(checkListItem, key);
  };

  const updateCheckListItemState = (checkListItem: ICheckListItem, key: string) => {
    const updatedCheckLists = checkLists.map((checkList) => {
      if (checkList.id === checkListItem.checkListId) {
        return {
          ...checkList,
          checkListItems: checkList.checkListItems.map((cli) => {
            if (cli.id === checkListItem.id) {
              return { ...cli, [key]: checkListItem[key] };
            }
            return { ...cli };
          }),
        };
      }
      return { ...checkList };
    });
    setCheckLists(updatedCheckLists);
  };

  const addCheckListItem = (checkListItem: ICheckListItem) => {
    const updatedCheckLists = checkLists.map((checkList) => {
      if (checkList.id === checkListItem.checkListId) {
        return {
          ...checkList,
          checkListItems: [...checkList.checkListItems, checkListItem],
        };
      }
      return { ...checkList };
    });
    setCheckLists(updatedCheckLists);
  };

  return (
    <div>
      {isLoading && (
        <div className="my-2">
          <Spinner message="Loading Checklists..." />
        </div>
      )}
      {!isLoading && (
        <>
          <h3 className="font-bold flex items-center">
            <BsCardChecklist className="mr-2" /> Checklists
          </h3>
          <div className="my-2 max-w-[500px] w-full">
            {checkLists.map((checkList) => {
              return (
                <CardCheckList
                  deleteCheckListItem={deleteCheckListItem}
                  addCheckListItem={addCheckListItem}
                  updateCheckListItem={updateCheckListItem}
                  key={checkList.id}
                  checkList={checkList}
                />
              );
            })}
          </div>
        </>
      )}
    </div>
  );
};

export default CardCheckLists;
