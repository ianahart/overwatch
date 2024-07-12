import { BsFillLightningFill } from 'react-icons/bs';
import { IConnection } from '../../../../../interfaces';
import DashboardAvatar from '../../../DashboardAvatar';
import { FaMapPin } from 'react-icons/fa';
import { useDispatch, useSelector } from 'react-redux';
import { TRootState, clearSelectedReviewer, setSelectedReviewer } from '../../../../../state/store';

export interface IReviewerProps {
  data: IConnection;
}

const Reviewer = ({ data }: IReviewerProps) => {
  const dispatch = useDispatch();

  const { selectedReviewer } = useSelector((store: TRootState) => store.addReview);

  const handleOnSelectReviewer = () => {
    if (selectedReviewer.id === data.id) {
      dispatch(clearSelectedReviewer());
      return;
    }
    dispatch(setSelectedReviewer({ reviewer: data }));
  };

  const highlightLightning = selectedReviewer.id === data.id ? 'text-green-400' : 'text-gray-400';

  return (
    <li className="my-4 p-2">
      <div className="flex items-center">
        <DashboardAvatar width="w-9" height="h-9" url={data.avatarUrl} abbreviation="?.?" />
        <div>
          <p className="ml-2">
            {data.firstName} {data.lastName}
          </p>
          <div className="flex items-center ml-2">
            <FaMapPin className="text-red-400" />
            <p className="text-sm">
              {data?.city}, {data?.country}
            </p>
          </div>
        </div>
      </div>

      <div className="my-1 flex justify-end">
        <button onClick={handleOnSelectReviewer} className="text-gray-400 flex items-center">
          <BsFillLightningFill className={`${highlightLightning}`} />
          {selectedReviewer.id === data.id ? 'Unselect' : 'Select'}
        </button>
      </div>
    </li>
  );
};

export default Reviewer;
