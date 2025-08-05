import { BiSolidEdit, BiTrash } from 'react-icons/bi';
import { ICheckListItem } from '../../../../../interfaces';
import { useEffect, useState } from 'react';

export interface ICardCheckListItemProps {
  checkListItem: ICheckListItem;
  updateCheckListItem: (checkListItem: ICheckListItem, key: string) => void;
  deleteCheckListItem: (checkListItem: ICheckListItem) => void;
}

const CardCheckListItem = ({ checkListItem, updateCheckListItem, deleteCheckListItem }: ICardCheckListItemProps) => {
  const [isEditOpen, setIsEditOpen] = useState(false);
  const [inputValue, setInputValue] = useState('');

  useEffect(() => {
    if (checkListItem.title !== null) {
      setInputValue(checkListItem.title);
    }
  }, [checkListItem.title]);

  const handleOnBlur = () => {
    setIsEditOpen(false);
    updateCheckListItem({ ...checkListItem, title: inputValue }, 'title');
  };

  const handleOnChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { checked } = e.target;
    updateCheckListItem({ ...checkListItem, isCompleted: checked }, 'isCompleted');
  };

  return (
    <div data-testid="card-checklist-item" className="flex justify-between my-2">
      <div className="flex items-center">
        {isEditOpen ? (
          <input
            className="border border-gray-800 rounded bg-transparent"
            onBlur={handleOnBlur}
            onChange={(e) => setInputValue(e.target.value)}
            type="text"
            value={inputValue}
          />
        ) : (
          <>
            <input onChange={handleOnChange} className="mr-2" type="checkbox" checked={checkListItem.isCompleted} />
            <p>{checkListItem.title}</p>
          </>
        )}
      </div>
      <div className="flex items-center">
        <div onClick={() => setIsEditOpen((prevState) => !prevState)} className="mx-1 cursor-pointer">
          <BiSolidEdit />
        </div>
        <div className="mx-1 cursor-pointer" onClick={() => deleteCheckListItem(checkListItem)}>
          <BiTrash />
        </div>
      </div>
    </div>
  );
};

export default CardCheckListItem;
