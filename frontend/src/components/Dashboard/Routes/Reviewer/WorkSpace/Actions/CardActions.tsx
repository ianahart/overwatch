import { ITodoCard } from '../../../../../../interfaces';
import CardRemoveBtn from './CardRemoveBtn';

export interface ICardActionsProps {
  card: ITodoCard;
}

const CardActions = ({ card }: ICardActionsProps) => {
  return (
    <div>
      <div>
        <h3 className="font-bold">Actions</h3>
      </div>
      <CardRemoveBtn card={card} />
    </div>
  );
};

export default CardActions;
