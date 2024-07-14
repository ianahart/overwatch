import { useSelector } from 'react-redux';
import { AiOutlineInfoCircle } from 'react-icons/ai';

import DashboardAvatar from '../../../DashboardAvatar';
import { TRootState } from '../../../../../state/store';

const ChosenReviewer = () => {
  const { selectedReviewer } = useSelector((store: TRootState) => store.addReview);

  return (
    <>
      {selectedReviewer.id !== 0 ? (
        <div>
          <p className="text-lg text-gray-400">You have selected</p>
          <div className="flex items-center">
            <DashboardAvatar width="w-9" height="h-9" url={selectedReviewer.avatarUrl} abbreviation="?.?" />
            <div>
              <p className="ml-2">
                {selectedReviewer.firstName} {selectedReviewer.lastName}
              </p>
            </div>
          </div>
          <p className="text-lg text-gray-400">to review your code</p>
        </div>
      ) : (
        <div className="flex items-center">
          <AiOutlineInfoCircle className="text-yellow-400 mr-2" />
          <p className="text-gray-400">You have not chosen a reviewer to review your code yet.</p>
        </div>
      )}
    </>
  );
};

export default ChosenReviewer;
