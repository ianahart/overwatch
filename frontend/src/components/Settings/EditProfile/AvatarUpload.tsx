import { useEffect, useState } from 'react';
import { BsPencil } from 'react-icons/bs';
import { useDispatch, useSelector } from 'react-redux';
import {
  TRootState,
  updateAvatar,
  updateUser,
  useCreateAvatarMutation,
  useRemoveAvatarMutation,
} from '../../../state/store';

const AvatarUpload = () => {
  const [createAvatar] = useCreateAvatarMutation();
  const [removeAvatar] = useRemoveAvatarMutation();
  const dispatch = useDispatch();
  const { avatar } = useSelector((store: TRootState) => store.profileSetup);
  const { user, token } = useSelector((store: TRootState) => store.user);
  const [error, setError] = useState('');

  const avatarImage = typeof avatar.value === 'string' && avatar.value.length ? `url(${avatar.value})` : 'unset';

  const handleRemoveAvatar = () => {
    removeAvatar({ profileId: user.profileId, token, avatarUrl: null, avatarFilename: null })
      .unwrap()
      .then(() => {
        dispatch(updateAvatar(null));
        dispatch(updateUser({ ...user, avatarUrl: '' }));
      });
  };

  useEffect(() => {
    if (avatar.value instanceof File) {
      const formData = new FormData();
      formData.append('avatar', avatar.value as File);
      createAvatar({ token, profileId: user.profileId, formData })
        .unwrap()
        .then((res) => {
          dispatch(updateUser({ ...user, avatarUrl: res.data.avatarUrl }));
          dispatch(updateAvatar(res.data.avatarUrl));
        })
        .catch((err) => {
          console.log(err);
          setError(err.data.message);
        });
    }
  }, [avatar]);

  const validateFileSize = (file: File) => {
    let exceedsFileSize = false;
    if (file.size > 2000000) {
      exceedsFileSize = true;
    }
    return exceedsFileSize;
  };

  const handleOnDrop = (e: React.DragEvent<HTMLDivElement>) => {
    e.stopPropagation();
    e.preventDefault();

    setError('');
    const { files } = e.dataTransfer;

    if (!files) return;

    if (validateFileSize(files[0])) {
      setError('Profile picture exceeds 2MB limit');
      return;
    }
    dispatch(updateAvatar(files[0]));
  };

  const handleOnChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setError('');
    if (e.target.files === null) return;

    const file = e.target.files[0];

    if (validateFileSize(file)) {
      setError('Profile picture exceeds 2MB limit');
      return;
    }
    dispatch(updateAvatar(file));
  };

  return (
    <div className="my-8">
      <p>Avatar</p>

      <div
        style={{ backgroundImage: avatarImage, backgroundPosition: 'center' }}
        draggable
        onDrop={handleOnDrop}
        className="mb-8 mt-4 relative rounded-full border border-gray-800 w-32 h-32"
      >
        <div className="cursor-pointer absolute right-0 bottom-0 bg-gray-800 rounded p-2">
          <BsPencil className="" />
        </div>
        <input
          aria-label="Avatar upload"
          onChange={handleOnChange}
          name="avatar"
          id="avatar"
          type="file"
          accept="image/*"
          className="h-full w-full absolute z-10 opacity-0"
        />
      </div>
      {error.length > 0 && (
        <div className="my-4">
          <p className="text-sm text-red-400">{error}</p>
        </div>
      )}

      {avatarImage !== 'unset' && (
        <button onClick={handleRemoveAvatar} className="outline-btn border border-gray-700 !text-gray-400 w-24">
          Remove
        </button>
      )}
    </div>
  );
};

export default AvatarUpload;
