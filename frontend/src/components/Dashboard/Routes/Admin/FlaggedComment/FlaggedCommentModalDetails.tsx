import dayjs from 'dayjs';
import { IReportComment } from '../../../../../interfaces';
import Avatar from '../../../../Shared/Avatar';
import { AiOutlineCalendar, AiOutlineInfoCircle, AiOutlineUser } from 'react-icons/ai';
import { useSelector } from 'react-redux';
import { TRootState, useDeleteReportCommentMutation } from '../../../../../state/store';

export interface IFlaggedCommentModalDetailsProps {
  reportComment: IReportComment;
  handleCloseModal: () => void;
  updateStatus: (id: number, status: string) => void;
}

const FlaggedCommentModalDetails = ({
  reportComment,
  updateStatus,
  handleCloseModal,
}: IFlaggedCommentModalDetailsProps) => {
  const { token } = useSelector((store: TRootState) => store.user);
  const [deleteReportComment] = useDeleteReportCommentMutation();

  const handleOnDelete = () => {
    deleteReportComment({ id: reportComment.id, token })
      .unwrap()
      .then((res) => {
        console.log(res);
        updateStatus(reportComment.id, 'DELETED');
        handleCloseModal();
      })
      .catch((err) => {
        console.log(err);
      });
  };

  return (
    <div>
      <header className="flex items-center flex-col my-4">
        <h3 className="text-xl">Delete Flagged Comment</h3>
        <p>
          Comment under topic <span className="text-xl font-bold">{reportComment.topicTitle}</span>
        </p>
      </header>
      <div className="my-8">
        <div className="text-sm">
          <div className="flex items-center">
            <AiOutlineInfoCircle className="text-yellow-400 mr-2" />
            <p>Comment under investigation</p>
          </div>
          <div className="flex items-center">
            <AiOutlineUser className="mr-2" />
            <p>reported by {reportComment.reportedBy}</p>
          </div>
          <div className="flex items-center">
            <AiOutlineCalendar className="mr-2" />
            <p className="text-xs">{dayjs(reportComment.createdAt).format('MM/D/YYYY')}</p>
          </div>
        </div>
        <div className="my-8">
          <Avatar width="w-9" height="h-9" initials="?.?" avatarUrl={reportComment.commentAvatarUrl} />
          <p className="text-sm leading-6">&quot;{reportComment.content}&quot;</p>
        </div>
        <div className="my-8">
          <p className="text-sm">Reason for reporting</p>
          <p className="text-xs p-1 rounded text-white bg-blue-400 inline-flex">{reportComment.reason}</p>
          <p className="text-sm my-2 leading-6">{reportComment.details}</p>
        </div>
      </div>
      {reportComment.status === 'ACTIVE' && (
        <div className="flex justify-evenly">
          <button onClick={handleOnDelete} className="btn">
            Delete
          </button>
          <button className="outline-btn border rounded border-gray-800 !text-gray-400" onClick={handleCloseModal}>
            Cancel
          </button>
        </div>
      )}
    </div>
  );
};

export default FlaggedCommentModalDetails;
