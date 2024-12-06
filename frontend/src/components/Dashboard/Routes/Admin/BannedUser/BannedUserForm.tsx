import { useEffect, useState } from 'react';
import { useSelector } from 'react-redux';

import BannedUserFormSearch from './BannedUserFormSearch';
import { IBan, IError, IReviewer } from '../../../../../interfaces';
import SelectedUserBlock from './SelectedUserBlock';
import {
  TRootState,
  useCreateBannedUserMutation,
  useLazyFetchBannedUserQuery,
  useUpdateBannedUserMutation,
} from '../../../../../state/store';

export interface IBannedUserFormProps {
  formType: string;
  handleSetView: (view: string) => void;
  banId?: number;
  updateBannedUserState?: (ban: IBan) => void;
}

export type FormElement = HTMLTextAreaElement | HTMLSelectElement;

const BannedUserForm = ({
  formType,
  handleSetView,
  banId = 0,
  updateBannedUserState = () => {},
}: IBannedUserFormProps) => {
  const { token } = useSelector((store: TRootState) => store.user);
  const [createBannedUser] = useCreateBannedUserMutation();
  const [updateBannedUser] = useUpdateBannedUserMutation();
  const [fetchBannedUser] = useLazyFetchBannedUserQuery();
  const [adminNotes, setAdminNotes] = useState('');
  const [error, setError] = useState('');
  const [time, setTime] = useState(86400);
  const [selectedUser, setSelectedUser] = useState<IReviewer>({ id: 0, fullName: '', avatarUrl: '' });

  useEffect(() => {
    if (banId !== 0) {
      fetchBannedUser({ banId, token })
        .unwrap()
        .then((res) => {
          const { time, adminNotes, fullName, userId } = res.data;
          setSelectedUser((prevState) => ({ ...prevState, fullName, id: userId }));
          setTime(time);
          setAdminNotes(adminNotes);
        })
        .catch((err) => {
          console.log(err);
        });
    }
  }, [banId, token]);

  const applyErrors = <T extends IError>(data: T): void => {
    for (let prop in data) {
      setError(data[prop]);
    }
  };

  const handleOnChange = (e: React.ChangeEvent<FormElement>, type: string) => {
    const { value } = e.target;
    if (type === 'textarea') {
      setAdminNotes(value);
      return;
    }
    if (type === 'select') {
      setTime(Number.parseInt(value));
    }
  };

  const handleCreateBannedUser = (): void => {
    const payload = {
      userId: selectedUser.id,
      token,
      adminNotes,
      time,
    };
    createBannedUser(payload)
      .unwrap()
      .then((res) => {
        console.log(res);
        handleSetView('list');
      })
      .catch((err) => {
        console.log(err);
        applyErrors(err.data);
      });
  };

  const handleUpdateBannedUser = (): void => {
    console.log('updating banned user');
    const payload = {
      banId,
      token,
      adminNotes,
      time,
    };
    updateBannedUser(payload)
      .unwrap()
      .then((res) => {
        updateBannedUserState(res.data);
        handleSetView('list');
      })
      .catch((err) => {
        console.log(err);
      });
  };

  const handleOnSubmit = (e: React.FormEvent<HTMLFormElement>): void => {
    e.preventDefault();

    if (formType === 'create') {
      handleCreateBannedUser();
      return;
    }

    if (formType === 'edit') {
      handleUpdateBannedUser();
    }
  };

  const handleSetSelectedUser = (user: IReviewer) => {
    setSelectedUser(user);
  };

  return (
    <div className="p-2 border border-gray-800 rounded max-w-[650px] w-full mx-auto">
      <form onSubmit={handleOnSubmit}>
        <div className="flex justify-center my-4">
          <h3 className="text-xl font-bold">Ban A User</h3>
        </div>
        {error.length > 0 && (
          <div className="my-4">
            <p className="text-xs text-red-300">{error}</p>
          </div>
        )}
        <SelectedUserBlock selectedUser={selectedUser} />
        {banId === 0 && <BannedUserFormSearch handleSetSelectedUser={handleSetSelectedUser} />}
        <div className="flex flex-col my-4">
          <label htmlFor="notes">Admin Notes</label>
          <textarea
            onChange={(e) => handleOnChange(e, 'textarea')}
            value={adminNotes}
            className="h-20 bg-transparent border p-2 rounded border-gray-800 resize-none"
            id="notes"
            name="notes"
          ></textarea>
        </div>
        <div className="flex flex-col my-4">
          <label htmlFor="time">Ban Time</label>
          <select
            className="bg-transparent  border border-gray-800 h-9"
            id="time"
            name="time"
            value={time}
            onChange={(e) => handleOnChange(e, 'select')}
          >
            <option value="86400">One Day</option>
            <option value="172800">Two Days</option>
            <option value="604800">One Week</option>
            <option value="1209600">Two Weeks</option>
            <option value="2592000">One Month</option>
            <option value="31536000">One Year</option>
          </select>
        </div>
        <div className="my-8">
          <button type="submit" className="btn w-full">
            {formType === 'create' ? 'Create' : 'Update'}
          </button>
        </div>
      </form>
    </div>
  );
};

export default BannedUserForm;
