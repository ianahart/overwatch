import { ITodoCard } from '../../../../../interfaces';
import Card from './Card';

export interface ICardListProps {
  cards: ITodoCard[];
}

const CardList = ({ cards }: ICardListProps) => {
  return (
    <ul>
      {cards.map((card) => {
        return <Card key={card.id} data={card} />;
      })}
    </ul>
  );
};

export default CardList;
