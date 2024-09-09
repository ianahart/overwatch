import { useSelector } from 'react-redux';

import { ITodoCard } from '../../../../../../interfaces';
import { TRootState, useDeleteLabelMutation, useFetchActiveLabelsQuery } from '../../../../../../state/store';
import CardLabel from './CardLabel';
import { useEffect, useState } from 'react';

export interface ICardLabelsProps {
  card: ITodoCard;
  handleOnOpenLabelForm: () => void;
}

const CardLabels = ({ card, handleOnOpenLabelForm }: ICardLabelsProps) => {
  const { token } = useSelector((store: TRootState) => store.user);
  const { labels } = useSelector((store: TRootState) => store.workSpace);
  const [deleteLabelMut] = useDeleteLabelMutation();
  const [activeLabelIds, setActiveLabelIds] = useState<number[]>([]);
  const { data: activeLabelsData } = useFetchActiveLabelsQuery({ token, todoCardId: card.id });

  useEffect(() => {
    if (activeLabelsData !== undefined) {
      const activeLabelIdsData = activeLabelsData.data.map((activeLabel) => activeLabel.labelId);
      setActiveLabelIds(activeLabelIdsData);
    }
  }, [activeLabelsData]);

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
            return (
              <CardLabel
                activeLabelIds={activeLabelIds}
                card={card}
                label={label}
                key={label.id}
                handleOnDeleteLabel={handleOnDeleteLabel}
              />
            );
          })}
        </div>
      </div>
    </>
  );
};

export default CardLabels;
