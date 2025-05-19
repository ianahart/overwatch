import { useState } from 'react';
import { ITag } from '../../interfaces';
import { AiOutlineEdit } from 'react-icons/ai';
import { BsTrash } from 'react-icons/bs';
import ToolTip from '../Shared/ToolTip';

export interface ITagEditItemProps {
  tag: ITag;
  handleUpdateTag: (name: string, id: number) => void;
  handleDeleteTag: (id: number) => void;
}

const TagEditItem = ({ tag, handleUpdateTag, handleDeleteTag }: ITagEditItemProps) => {
  const [inputValue, setInputValue] = useState(tag.name);
  const [isEditing, setIsEditing] = useState(false);

  const closeInput = (): void => {
    setIsEditing(false);
    setInputValue(tag.name);
  };

  const handleOnUpdate = (e: React.MouseEvent<HTMLButtonElement>): void => {
    e.stopPropagation();
    handleUpdateTag(inputValue, tag.id);
    setIsEditing(false);
  };

  const handleOnDelete = () => {
    handleDeleteTag(tag.id);
  };

  return (
    <div className="my-4">
      {isEditing ? (
        <>
          <div className="flex flex-col">
            <label htmlFor={`tag${tag.id}`}>Edit tag name</label>
            <input
              value={inputValue}
              onChange={(e) => setInputValue(e.target.value)}
              className="h-9 bg-transparent rounded border border-gray-800"
              id={`tag${tag.id}`}
              name={`tag${tag.id}`}
            />
          </div>
          <div className="my-2">
            <button type="button" onClick={handleOnUpdate} className="btn">
              Edit
            </button>
            <button type="button" className="ml-4" onClick={closeInput}>
              Cancel
            </button>
          </div>
        </>
      ) : (
        <div className="flex justify-between">
          <div>
            <p>#{tag.name}</p>
          </div>
          <div className="flex items-center">
            <ToolTip message="Edit">
              <AiOutlineEdit onClick={() => setIsEditing(true)} className="cursor-pointer mx-2" />
            </ToolTip>
            <ToolTip message="Delete">
              <BsTrash data-testid="delete-tag-icon" onClick={handleOnDelete} className="cursor-pointer mx-2" />
            </ToolTip>
          </div>
        </div>
      )}
    </div>
  );
};

export default TagEditItem;
