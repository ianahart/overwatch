import { useState } from 'react';
import { IAdminBadge } from '../../../../../interfaces';
import DetailsModal from '../DetailsModal';
import BadgeForm from './BadgeForm';
import { useSelector } from 'react-redux';
import { TRootState, useDeleteBadgeMutation } from '../../../../../state/store';

export interface IBadgeListItemProps {
  badge: IAdminBadge;
}

const BadgeListItem = ({ badge }: IBadgeListItemProps) => {
  const { token } = useSelector((store: TRootState) => store.user);
  const [deleteBadge] = useDeleteBadgeMutation();
  const [isModalOpen, setIsModalOpen] = useState(false);

  const handleOnCloseModal = (): void => {
    setIsModalOpen(false);
  };

  const handleDeleteBadge = () => {
    deleteBadge({ token, badgeId: badge.id })
      .unwrap()
      .then(() => {})
      .catch((err) => {
        console.log(err);
      });
  };

  return (
    <li className="mx-4 my-6 w-full max-w-[250px] ">
      <img className="h-36 w-36 mx-auto" src={badge.imageUrl} alt={badge.title} />
      <h3 className="text-center text-gray-400 font-bold">{badge.title}</h3>
      <p className="italic text-sm text-center">{badge.description}</p>
      <div className="flex items-center justify-evenly my-2">
        <button
          onClick={() => setIsModalOpen(true)}
          className="border rounded border-gray-800 text-blue-400 px-1 cursor-pointer"
        >
          Update
        </button>
        <button onClick={handleDeleteBadge} className="border rounded border-gray-800 text-red-300 px-1 cursor-pointer">
          Delete
        </button>
      </div>
      {isModalOpen && (
        <DetailsModal handleCloseModal={handleOnCloseModal}>
          <BadgeForm formType="edit" badgeId={badge.id} handleCloseModal={handleOnCloseModal} />
        </DetailsModal>
      )}
    </li>
  );
};

export default BadgeListItem;
