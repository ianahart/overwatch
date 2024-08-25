import { AiOutlinePlus } from 'react-icons/ai';

const AddCard = () => {
  return (
    <div className="my-2 flex items-center p-1 rounded cursor-pointer">
      <div className="mr-2">
        <AiOutlinePlus />
      </div>
      <div>
        <p>Add card</p>
      </div>
    </div>
  );
};
export default AddCard;
