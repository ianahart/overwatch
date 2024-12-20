import dayjs from 'dayjs';
import { IBadge } from '../../../../../interfaces';

export interface IReviewerdataListItemProps {
  data: IBadge;
}

const dataListItem = ({ data }: IReviewerdataListItemProps) => {
  return (
    <li className="mx-4 my-6 w-full max-w-[250px] ">
      <img className="h-36 w-36 mx-auto" src={data.imageUrl} alt={data.title} />
      <p className="text-blue-400 mx-auto text-center text-xs my-1">
        <span>Earned on: </span>
        {dayjs(data.createdAt).format('MM/DD/YYYY')}
      </p>
      <h3 className="text-center text-gray-400 font-bold">{data.title}</h3>
      <p className="italic text-sm text-center">{data.description}</p>
    </li>
  );
};

export default dataListItem;
