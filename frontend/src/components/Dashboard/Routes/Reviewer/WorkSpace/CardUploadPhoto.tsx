import { useState } from 'react';
import { ITodoCard } from '../../../../../interfaces';
import { AiOutlineClose } from 'react-icons/ai';
import { useDispatch, useSelector } from 'react-redux';
import { TRootState, updateTodoListTodoCard, useUpdateTodoCardMutation } from '../../../../../state/store';

export interface ICardUploadPhotoProps {
  card: ITodoCard;
}

const CardUploadPhoto = ({ card }: ICardUploadPhotoProps) => {
  const { token } = useSelector((store: TRootState) => store.user);
  const dispatch = useDispatch();
  const [isImgHovered, setIsImgHovered] = useState(false);
  const [isToolTipHovered, setIsToolTipHovered] = useState(false);
  const [updateTodoCardMut] = useUpdateTodoCardMutation();

  const handleOnClick = () => {
    const payload = { ...card, uploadPhotoUrl: null };

    updateTodoCardMut({ token, card: payload })
      .unwrap()
      .then((res) => {
        dispatch(updateTodoListTodoCard(res.data));
      });
  };

  return (
    <div className="my-4 flex justify-center">
      <div
        onMouseEnter={() => setIsImgHovered(true)}
        onMouseLeave={() => setIsImgHovered(false)}
        className="relative rounded"
      >
        {card.uploadPhotoUrl !== null && card.uploadPhotoUrl && (
          <img
            className="rounded w-full md:w-[300px] md:h-[300px]"
            src={card.uploadPhotoUrl}
            alt="card uploaded photo"
          />
        )}
        {isImgHovered && (
          <div
            style={{ background: 'rgba(0,0,0,0.8)' }}
            className="inset-0 absolute rounded flex flex-col items-center justify-center"
          >
            <div className="relative w-full flex justify-center">
              {isToolTipHovered && (
                <div className="absolute bg-gray-800 p-2 -top-8 right-10 rounded">
                  <p className="text-xs font-bold">Remove image</p>
                </div>
              )}
              <div
                data-testid="card-upload-photo-close-icon"
                onClick={handleOnClick}
                onMouseEnter={() => setIsToolTipHovered(true)}
                onMouseLeave={() => setIsToolTipHovered(false)}
              >
                <AiOutlineClose className="text-gray-400 text-4xl cursor-pointer" />
              </div>
            </div>
          </div>
        )}
      </div>
    </div>
  );
};

export default CardUploadPhoto;
