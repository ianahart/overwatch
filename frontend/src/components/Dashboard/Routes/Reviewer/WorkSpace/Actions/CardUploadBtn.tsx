import { AiOutlineClose, AiOutlineUpload } from 'react-icons/ai';
import { useState } from 'react';
import { filesize } from 'filesize';
import { useDispatch, useSelector } from 'react-redux';

import { ITodoCard } from '../../../../../../interfaces';
import ClickAway from '../../../../../Shared/ClickAway';
import { TRootState, updateTodoListTodoCard, useUploadTodoCardPhotoMutation } from '../../../../../../state/store';

export interface ICardUploadBtnProps {
  card: ITodoCard;
}

type TFile = File | null;

const CardUploadBtn = ({ card }: ICardUploadBtnProps) => {
  const dispatch = useDispatch();
  const MAX_FILE_SIZE = 1000000;
  const { token } = useSelector((store: TRootState) => store.user);
  const [clickAwayOpen, setClickAwayOpen] = useState(false);
  const [error, setError] = useState('');
  const [uploadTodoCardPhotoMut] = useUploadTodoCardPhotoMutation();

  const handleOnClickAwayOpen = () => {
    setClickAwayOpen(true);
  };

  const handleOnClickAwayClose = () => {
    setClickAwayOpen(false);
  };

  const handleOnChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setError('');
    const { files } = e.target;

    if (!files) {
      return;
    }

    const [file] = files;

    if (file.size > MAX_FILE_SIZE) {
      const curFileSize = filesize(file.size);
      setError(`Max file size is 1MB(Megabyte). Current file is: ${curFileSize}`);
      return;
    }
    uploadPhoto(file);
  };

  const uploadPhoto = (file: TFile) => {
    const formData = new FormData();
    if (file) {
      formData.append('file', file);
      uploadTodoCardPhotoMut({ token, todoCardId: card.id, formData })
        .unwrap()
        .then((res) => {
          dispatch(updateTodoListTodoCard(res.data));
          handleOnClickAwayClose();
        })
        .catch((err) => {
          setError(err.data.message);
        });
    }
  };

  return (
    <div className="p-1 bg-gray-800 rounded w-32 my-2 hover:bg-gray-700 relative">
      <button onClick={handleOnClickAwayOpen} className="flex items-center text-sm">
        <AiOutlineUpload className="mr-2" />
        Upload
      </button>
      {clickAwayOpen && (
        <ClickAway onClickAway={handleOnClickAwayClose}>
          <div className="p-2 rounded bg-gray-700 overflow-x-hidden absolute z-20 right-0 w-[250px] h-32 overflow-y-auto top-0">
            <div>
              <div className="flex justify-between">
                <div>&nbsp;</div>
                <h3 className="text-center text-sm">Upload Image</h3>
                <div
                  data-testid="card-upload-close-btn"
                  onClick={handleOnClickAwayClose}
                  className="cursor-pointer hover:opacity-70"
                >
                  <AiOutlineClose />
                </div>
              </div>
              {error.length > 0 ? (
                <p className="text-xs text-center my-1 text-red-300">{error}</p>
              ) : (
                <>
                  <p className="text-xs">Here you can upload an image that you choose</p>
                  <small className="text-xs">Max upload size is 1MB. (Megabyte)</small>
                  {card.uploadPhotoUrl !== null && (
                    <div className="my-1 flex justify-center">
                      <img className="h-9 w-9 rounded" src={card.uploadPhotoUrl} alt="uploaded photo" />
                    </div>
                  )}
                </>
              )}
            </div>
            <div>
              <form>
                <div className="relative">
                  <div className="flex justify-center">
                    <button className="p-1 rounded bg-gray-800 text-sm hover:bg-gray-700 cursor-pointer">
                      Upload here
                    </button>
                  </div>
                  <input
                    onChange={handleOnChange}
                    data-testid="card-upload-input"
                    className="absolute h-full w-full z-10 -top-0 opacity-0 cursor-pointer"
                    id="upload"
                    name="upload"
                    type="file"
                    accept="image/jpeg, image/png"
                  />
                </div>
              </form>
            </div>
          </div>
        </ClickAway>
      )}
    </div>
  );
};

export default CardUploadBtn;
