import { AiOutlineCloseCircle } from 'react-icons/ai';
import { IFormTopicTag } from '../../interfaces';

export interface ICreateCommunityTagItemProps {
  tag: IFormTopicTag;
  removeTag: (id: string) => void;
}

const CreateCommunityTagItem = ({ tag, removeTag }: ICreateCommunityTagItemProps) => {
  const handleOnClick = () => {
    removeTag(tag.id);
  };

  return (
    <div className="relative mx-2 p-2 bg-blue-400 rounded-xl">
      <div data-testid="tag-item-container" onClick={handleOnClick} className="absolute top-0 right-0 z-10">
        <AiOutlineCloseCircle role="close-icon" className="text-black cursor-pointer" />
      </div>
      <p className="text-black break-all">#{tag.name}</p>
    </div>
  );
};

export default CreateCommunityTagItem;
