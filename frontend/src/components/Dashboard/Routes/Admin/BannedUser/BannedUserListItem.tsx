import dayjs from 'dayjs';
import { useSelector } from 'react-redux';
import { FaHourglassEnd, FaHourglassStart } from 'react-icons/fa';
import { GiDuration } from 'react-icons/gi';
import { useState } from 'react';

import { IBan } from '../../../../../interfaces';
import DetailsModal from '../DetailsModal';
import BannedUserForm from './BannedUserForm';
import { TRootState, useDeleteBannedUserMutation } from '../../../../../state/store';

export interface IBannedUserListItemProps {
  user: IBan;
  handleSetView: (view: string) => void;
  updateBannedUserState: (ban: IBan) => void;
  removeBannedUserState: (banId: number) => void;
}

const BannedUserListItem = ({
  user,
  handleSetView,
  updateBannedUserState,
  removeBannedUserState,
}: IBannedUserListItemProps) => {
  const { token } = useSelector((store: TRootState) => store.user);
  const [deleteBannedUser] = useDeleteBannedUserMutation();
  const [detailModalOpen, setDetailModalOpen] = useState(false);
  const [editModalOpen, setEditModalOpen] = useState(false);

  const handleCloseDetailModal = () => {
    setDetailModalOpen(false);
  };

  const handleCloseEditModal = () => {
    setEditModalOpen(false);
  };

  const handleUnBan = (): void => {
    const payload = { token, banId: user.id };
    deleteBannedUser(payload)
      .unwrap()
      .then(() => {
        removeBannedUserState(user.id);
      })
      .catch((err) => {
        console.log(err);
      });
  };

  return (
    <div className="flex justify-evenly my-4 p-2 border border-gray-800 rounded text-gray-400">
      <div className="mx-1">
        <p>{user.fullName}</p>
        <p className="text-sm font-bold">{user.email}</p>
      </div>
      <div className="mx-1 flex items-center">
        <FaHourglassStart className="text-green-400 mr-1" />
        <p>{dayjs(user.createdAt).format('MM/DD/YYYY')}</p>
      </div>
      <div className="mx-1 flex items-center">
        <FaHourglassEnd className="text-red-300 mr-1" />
        <p>{dayjs(user.banDate).format('MM/DD/YYYY')}</p>
      </div>
      <div className="mx-1 flex items-center">
        <GiDuration className="text-blue-400 text-lg mr-1" />
        <p>{user.time / 86400} days</p>
      </div>
      <div className="flex mx-1">
        <button onClick={handleUnBan} className="mx-1 btn !bg-gray-400">
          Unban
        </button>
        <button onClick={() => setDetailModalOpen(true)} className="mx-1 shadow-lg btn !bg-blue-400 !py-1">
          Details
        </button>
        <button onClick={() => setEditModalOpen(true)} className="mx-1 shadow-lg btn !py-1">
          Edit
        </button>
      </div>
      {detailModalOpen && (
        <DetailsModal handleCloseModal={handleCloseDetailModal}>
          <div>
            <p className="text-xl font-bold mb-2">Admin Notes</p>
            <p>{user.adminNotes}</p>
          </div>
        </DetailsModal>
      )}
      {editModalOpen && (
        <DetailsModal handleCloseModal={handleCloseEditModal}>
          <BannedUserForm
            formType="edit"
            handleSetView={handleSetView}
            banId={user.id}
            updateBannedUserState={updateBannedUserState}
          />
        </DetailsModal>
      )}
    </div>
  );
};

export default BannedUserListItem;
