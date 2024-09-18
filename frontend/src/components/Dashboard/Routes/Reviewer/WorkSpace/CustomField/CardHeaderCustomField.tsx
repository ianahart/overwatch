import { AiOutlineClose } from 'react-icons/ai';
import { BsChevronLeft } from 'react-icons/bs';

export interface ICardHeaderCustomFieldProps {
  page: number;
  handleCloseClickAway: () => void;
  navigatePrevPage: () => void;
}

const CardHeaderCustomField = ({ page, handleCloseClickAway, navigatePrevPage }: ICardHeaderCustomFieldProps) => {
  return (
    <div className="flex justify-between">
      <div>
        {page > 1 && (
          <div className="cursor-pointer" onClick={navigatePrevPage}>
            <BsChevronLeft />
          </div>
        )}
      </div>
      <div>
        <h3 className="font-bold">Custom Field</h3>
      </div>
      <div onClick={handleCloseClickAway} className="cursor-pointer">
        <AiOutlineClose className="hover:text-gray-500" />
      </div>
    </div>
  );
};

export default CardHeaderCustomField;
