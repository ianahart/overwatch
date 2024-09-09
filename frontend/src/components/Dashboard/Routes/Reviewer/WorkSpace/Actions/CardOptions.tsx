import { ITodoCard } from '../../../../../../interfaces';
import CardDatesBtn from './CardDatesBtn';
import CardLabelsBtn from './CardLabelsBtn';
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
      <CardLabelsBtn card={card} />
      <CardDatesBtn card={card} />
      <CardPhotoBtn card={card} />
    </div>
  );
};

export default CardOptions;
