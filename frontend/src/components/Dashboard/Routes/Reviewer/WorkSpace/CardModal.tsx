import { ITodoCard } from '../../../../../interfaces';
import ClickAway from '../../../../Shared/ClickAway';
import CardHeader from './CardHeader';

export interface ICardModalProps {
  handleOnModalClose: () => void;
  card: ITodoCard;
}

const CardModal = ({ handleOnModalClose, card }: ICardModalProps) => {
  return (
    <div className="absolute cursor-default z-30 top-0 left-0 inset-0 bg-black bg-opacity-75 w-full h-full flex items-center flex-col justify-center">
      <ClickAway onClickAway={handleOnModalClose}>
        <div className="bg-gray-900 shadow-lg p-2 min-h-[600px] max-w-[100%] w-full md:w-[700px] rounded">
          <CardHeader card={card} handleOnModalClose={handleOnModalClose} />
        </div>
      </ClickAway>
    </div>
  );
};
export default CardModal;
