import { BsTrash } from 'react-icons/bs';
import { useSelector } from 'react-redux';

import { ILabel, ITodoCard } from '../../../../../../interfaces';
import {
  TRootState,
  useCreateActiveLabelMutation,
  useDeleteActiveLabelMutation,
  useUpdateLabelMutation,
} from '../../../../../../state/store';

export interface ICardLabelProps {
  card: ITodoCard;
  label: ILabel;
  handleOnDeleteLabel: (id: number) => Promise<void>;
  activeLabelIds: number[];
}

const CardLabel = ({ card, label, handleOnDeleteLabel, activeLabelIds }: ICardLabelProps) => {
  const { token } = useSelector((store: TRootState) => store.user);
  const [createActiveLabelMut] = useCreateActiveLabelMutation();
  const [deleteActiveLabelMut] = useDeleteActiveLabelMutation();
  const [updateLabelMut] = useUpdateLabelMutation();

  const createActiveLabel = (checked: boolean) => {
    createActiveLabelMut({ token, cardId: card.id, labelId: label.id })
      .unwrap()
      .then(() => {
        updateLabel(checked);
      })
      .catch((err) => {
        console.log(err);
      });
  };

  const deleteActiveLabel = async (checked: boolean) => {
    try {
      deleteActiveLabelMut({ token, id: card.id, labelId: label.id })
        .unwrap()
        .then(() => {
          updateLabel(checked);
        });
    } catch (err) {
      console.log(err);
    }
  };

  const updateLabel = (checked: boolean) => {
    const payload = { ...label, isChecked: checked };

    updateLabelMut({ token, label: payload })
      .unwrap()
      .then(() => {})
      .catch((err) => {
        console.log(err);
      });
  };

  const handleOnChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { checked } = e.target;
    if (checked) {
      createActiveLabel(checked);
    } else {
      deleteActiveLabel(checked);
    }

    //    updateLabel(checked);
  };

  return (
    <div data-testid="CardLabel" key={label.id} className="flex items-center justify-between my-1">
      <input onChange={handleOnChange} checked={label.isChecked && activeLabelIds.includes(label.id)} type="checkbox" />
      <div style={{ background: label.color }} className="rounded p-1 w-[80%]">
        <p className="text-sm font-bold text-white">{label.title}</p>
      </div>
      {label.isChecked ? (
        <div></div>
      ) : (
        <div
          data-testid="delete-active-label-btn"
          onClick={() => handleOnDeleteLabel(label.id)}
          className="cursor-pointer"
        >
          <BsTrash />
        </div>
      )}
    </div>
  );
};

export default CardLabel;
