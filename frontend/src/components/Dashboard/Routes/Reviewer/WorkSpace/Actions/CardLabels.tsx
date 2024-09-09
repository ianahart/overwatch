import { useSelector } from 'react-redux';

import { ITodoCard } from '../../../../../../interfaces';
import { TRootState, useDeleteLabelMutation } from '../../../../../../state/store';
import CardLabel from './CardLabel';

export interface ICardLabelsProps {
  card: ITodoCard;
  handleOnOpenLabelForm: () => void;
}

const CardLabels = ({ card, handleOnOpenLabelForm }: ICardLabelsProps) => {
  const { token } = useSelector((store: TRootState) => store.user);
  const { labels } = useSelector((store: TRootState) => store.workSpace);
  const [deleteLabelMut] = useDeleteLabelMutation();

  const handleOnDeleteLabel = async (id: number) => {
    try {
      await deleteLabelMut({ token, id }).unwrap();
    } catch (err) {
      console.log(err);
    }
  };

  return (
    <>
      <div className="flex justify-center my-2">
        <button onClick={handleOnOpenLabelForm} className="text-sm py-1 px-4 bg-gray-800 rounded hover:opacity-70">
          Add a new label
        </button>
      </div>
      <div>
        <h3 className="font-bold text-xs">Current Labels</h3>
        <div className="my-2">
          {labels.map((label) => {
            return <CardLabel card={card} label={label} key={label.id} handleOnDeleteLabel={handleOnDeleteLabel} />;
          })}
        </div>
      </div>
    </>
  );
};

export default CardLabels;
