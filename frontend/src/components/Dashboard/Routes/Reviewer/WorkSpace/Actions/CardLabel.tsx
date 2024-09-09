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
}

const CardLabel = ({ card, label, handleOnDeleteLabel }: ICardLabelProps) => {
  const { token } = useSelector((store: TRootState) => store.user);
  const [createActiveLabelMut] = useCreateActiveLabelMutation();
  const [deleteActiveLabelMut] = useDeleteActiveLabelMutation();
  const [updateLabelMut] = useUpdateLabelMutation();

  const createActiveLabel = () => {
    createActiveLabelMut({ token, cardId: card.id, labelId: label.id })
      .unwrap()
      .then((res) => {
        console.log(res);
      })
      .catch((err) => {
        console.log(err);
      });
  };

  const deleteActiveLabel = async () => {
    try {
      await deleteActiveLabelMut({ token, id: card.id, labelId: label.id }).unwrap();
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
      createActiveLabel();
    } else {
      deleteActiveLabel();
    }

    updateLabel(checked);
  };

  return (
    <div key={label.id} className="flex items-center justify-between my-1">
      <input onChange={handleOnChange} checked={label.isChecked} type="checkbox" />
      <div style={{ background: label.color }} className="rounded p-1 w-[80%]">
        <p className="text-sm font-bold text-white">{label.title}</p>
      </div>
      <div onClick={() => handleOnDeleteLabel(label.id)} className="cursor-pointer">
        <BsTrash />
      </div>
    </div>
  );
};

export default CardLabel;
