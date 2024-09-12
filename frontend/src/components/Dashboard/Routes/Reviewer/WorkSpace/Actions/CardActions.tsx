import { ITodoCard } from '../../../../../../interfaces';
import CardMoveBtn from './CardMoveBtn';
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
      <CardMoveBtn card={card} />
      <CardRemoveBtn card={card} />
    </div>
  );
};

export default CardActions;
