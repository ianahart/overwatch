import { useEffect } from 'react';
import { ITodoCard } from '../../../../../interfaces';
import ClickAway from '../../../../Shared/ClickAway';
import CardDetails from './CardDetails';
import CardHeader from './CardHeader';
import CardUploadPhoto from './CardUploadPhoto';
import CardCheckLists from './CardCheckLists';

export interface ICardModalProps {
  handleOnModalClose: () => void;
  card: ITodoCard;
}

const CardModal = ({ handleOnModalClose, card }: ICardModalProps) => {
  useEffect(() => {
    return () => {
      localStorage.removeItem('details');
    };
  }, []);

  return (
    <div className="absolute cursor-default z-30 top-0 left-0 inset-0 bg-black bg-opacity-75 w-full h-full md:flex md:items-center md:justify-center md:flex-col ">
      <ClickAway onClickAway={handleOnModalClose}>
        <div className="bg-gray-900 shadow-lg p-2 min-h-[600px] max-w-[100%] w-[95%] mx-auto md:w-[700px] rounded">
          <CardHeader card={card} handleOnModalClose={handleOnModalClose} />
          <CardDetails card={card} />
          <CardUploadPhoto card={card} />
          <CardCheckLists card={card} />
        </div>
      </ClickAway>
    </div>
  );
};
export default CardModal;
