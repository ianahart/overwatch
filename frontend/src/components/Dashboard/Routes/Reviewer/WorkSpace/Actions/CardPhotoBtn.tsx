import { BsImage } from 'react-icons/bs';
import { useSelector, useDispatch } from 'react-redux';
import { useState } from 'react';

import ClickAway from '../../../../../Shared/ClickAway';
import { ITodoCard } from '../../../../../../interfaces';
import CardPhotosLibrary from './CardPhotosLibrary';
import { TRootState, updateTodoListTodoCard, useUpdateTodoCardMutation } from '../../../../../../state/store';

export interface ICardPhotoBtnProps {
  card: ITodoCard;
}

const CardPhotoBtn = ({ card }: ICardPhotoBtnProps) => {
  const dispatch = useDispatch();
  const { token } = useSelector((store: TRootState) => store.user);
  const [clickAwayOpen, setClickAwayOpen] = useState(false);
  const [updateTodoCardMut] = useUpdateTodoCardMutation();

  const handleOnClickAwayOpen = () => {
    setClickAwayOpen(true);
  };

  const handleOnClickAwayClose = () => {
    setClickAwayOpen(false);
  };

  const updateCardPhoto = (photo: string) => {
    const updatedCard = { ...card, photo };

    updateTodoCardMut({ token, card: updatedCard })
      .unwrap()
      .then((res) => {
        dispatch(updateTodoListTodoCard(res.data));
      })
      .catch((err) => {
        console.log(err);
      });
  };

  return (
    <div className="relative">
      <div className="p-1 bg-gray-800 rounded w-32 my-2 hover:bg-gray-700">
        <button onClick={handleOnClickAwayOpen} className="flex items-center text-sm">
          <BsImage className="mr-2" />
          Add photo
        </button>
      </div>
      {clickAwayOpen && (
        <ClickAway onClickAway={handleOnClickAwayClose}>
          <CardPhotosLibrary updateCardPhoto={updateCardPhoto} />
        </ClickAway>
      )}
    </div>
  );
};

export default CardPhotoBtn;
