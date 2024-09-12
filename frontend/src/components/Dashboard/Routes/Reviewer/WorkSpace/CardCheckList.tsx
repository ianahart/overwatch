import { useSelector } from 'react-redux';
import { GoChecklist } from 'react-icons/go';
import { useState } from 'react';

import { ICheckList, ICheckListItem } from '../../../../../interfaces';
import { TRootState, useDeleteCheckListMutation } from '../../../../../state/store';
import CardCheckListItemForm from './CardCheckListItemForm';
import CardCheckListItem from './CardCheckListItem';

export interface ICardCheckListProps {
  checkList: ICheckList;
  updateCheckListItem: (checkListItem: ICheckListItem, key: string) => void;
  deleteCheckListItem: (checkListItem: ICheckListItem) => void;
  addCheckListItem: (checkListItem: ICheckListItem) => void;
}

const CardCheckList = ({
  addCheckListItem,
  checkList,
  updateCheckListItem,
  deleteCheckListItem,
}: ICardCheckListProps) => {
  const { token } = useSelector((store: TRootState) => store.user);
  const [deleteCheckListMut, { isLoading }] = useDeleteCheckListMutation();
  const [formShowing, setFormShowing] = useState(false);

  const handleOnDeleteCheckList = () => {
    deleteCheckListMut({ token, id: checkList.id })
      .unwrap()
      .then((res) => {
        console.log(res);
      })
      .catch((err) => {
        console.log(err);
      });
  };

  const closeForm = () => {
    setFormShowing(false);
  };

  return (
    <div className="">
      <div className="flex justify-between">
        <div className="flex items-center">
          <div className="mr-2">
            <GoChecklist />
          </div>
          <div>
            <h3 className="text-xl">{checkList.title}</h3>
          </div>
        </div>
        {!isLoading && (
          <div>
            <button onClick={handleOnDeleteCheckList} className="p-1 bg-gray-800 rounded w-16 my-2 hover:bg-gray-700">
              Delete
            </button>
          </div>
        )}
      </div>
      <div className="flex items-center">
        <h3 className="mr-1">0%</h3>
        <div className="p-2 bg-gray-800 w-[80%] rounded-md"></div>
      </div>
      <div>
        {!formShowing && (
          <button onClick={() => setFormShowing(true)} className="p-1 bg-gray-800 rounded my-2 hover:bg-gray-700">
            Add Item
          </button>
        )}
        {formShowing && (
          <CardCheckListItemForm addCheckListItem={addCheckListItem} closeForm={closeForm} checkListId={checkList.id} />
        )}
      </div>
      {checkList.checkListItems.map((checkListItem) => {
        return (
          <CardCheckListItem
            deleteCheckListItem={deleteCheckListItem}
            updateCheckListItem={updateCheckListItem}
            key={checkListItem.id}
            checkListItem={checkListItem}
          />
        );
      })}
    </div>
  );
};

export default CardCheckList;
