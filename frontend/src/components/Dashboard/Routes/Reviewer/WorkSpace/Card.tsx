import { LuGrip } from 'react-icons/lu';
import { ITodoCard } from '../../../../../interfaces';

export interface ICardProps {
  data: ITodoCard;
}

const Card = ({ data }: ICardProps) => {
  return (
    <li className="my-6 bg-gray-800 p-2 rounded shadow-md cursor-pointer">
      <div className="flex justify-end">
        <LuGrip />
      </div>
      <div>
        <p>{data.title}</p>
      </div>
    </li>
  );
};

export default Card;
