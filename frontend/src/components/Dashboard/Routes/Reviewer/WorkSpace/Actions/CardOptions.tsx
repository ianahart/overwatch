import { ITodoCard } from '../../../../../../interfaces';
import CardCheckListBtn from './CardCheckListBtn';
import CardCustomFieldBtn from './CardCustomFieldBtn';
import CardDatesBtn from './CardDatesBtn';
import CardLabelsBtn from './CardLabelsBtn';
import CardPhotoBtn from './CardPhotoBtn';
import CardUploadBtn from './CardUploadBtn';

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
      <CardCheckListBtn card={card} />
      <CardDatesBtn card={card} />
      <CardUploadBtn card={card} />
      <CardPhotoBtn card={card} />
      <CardCustomFieldBtn card={card} />
    </div>
  );
};

export default CardOptions;
