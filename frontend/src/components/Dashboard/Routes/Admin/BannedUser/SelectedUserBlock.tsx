import { IReviewer } from '../../../../../interfaces';
import { initializeName } from '../../../../../util';
import Avatar from '../../../../Shared/Avatar';

export interface ISelectedUserBlockProps {
  selectedUser: IReviewer;
}

const SelectedUserBlock = ({ selectedUser }: ISelectedUserBlockProps) => {
  const [first, last] = selectedUser.fullName.split(' ');
  return (
    <>
      {selectedUser.id === 0 ? (
        <p>You haven't selected anyone yet.</p>
      ) : (
        <div className="bg-gray-950 rounded p-2">
          <p>You have selected:</p>
          <div className="flex items-center">
            <Avatar
              initials={initializeName(first, last)}
              height="h-9"
              width="w-9"
              avatarUrl={selectedUser.avatarUrl}
            />
            <div className="ml-1 text-gray-400">
              <p>{selectedUser.fullName}</p>
              <p>User Id: {selectedUser.id}</p>
            </div>
          </div>
        </div>
      )}
    </>
  );
};

export default SelectedUserBlock;
