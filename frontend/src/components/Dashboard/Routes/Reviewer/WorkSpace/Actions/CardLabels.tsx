import { ITodoCard } from '../../../../../../interfaces';

export interface ICardLabelsProps {
  card: ITodoCard;
  handleOnOpenLabelForm: () => void;
}

const CardLabels = ({ card, handleOnOpenLabelForm }: ICardLabelsProps) => {
  return (
    <div className="flex justify-center my-2">
      <button onClick={handleOnOpenLabelForm} className="text-sm py-1 px-4 bg-gray-800 rounded hover:opacity-70">
        Add a new label
      </button>
    </div>
  );
};

export default CardLabels;
