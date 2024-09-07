import { ITodoCard } from '../../../../../../interfaces';
import CardDatesBtn from './CardDatesBtn';
import CardPhotoBtn from './CardPhotoBtn';

export interface ICardOptionsProps {
  card: ITodoCard;
}

const CardOptions = ({ card }: ICardOptionsProps) => {
  return (
    <div>
      <div>
        <h3 className="font-bold">Add to card</h3>
      </div>
      <CardPhotoBtn card={card} />
      <CardDatesBtn card={card} />
    </div>
  );
};

export default CardOptions;
