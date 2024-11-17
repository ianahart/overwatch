import dayjs from 'dayjs';
import { IReportComment } from '../../../../../interfaces';
import { useState } from 'react';
import DetailsModal from '../DetailsModal';
import FlaggedCommentModalDetails from './FlaggedCommentModalDetails';

export interface IFlaggedCommentListItemProps {
  reportComment: IReportComment;
  updateStatus: (id: number, status: string) => void;
}

const FlaggedCommentListItem = ({ reportComment, updateStatus }: IFlaggedCommentListItemProps) => {
  const [isModalOpen, setIsModalOpen] = useState(false);

  const handleCloseModal = (): void => {
    setIsModalOpen(false);
  };

  return (
    <div className="p-2 rounded border border-gray-800 text-gray-400 relative my-2">
      <div className="flex-wrap flex items-center justify-between">
        <div className="text-xs">
          <p>reported by</p>
          <p>{reportComment.reportedBy}</p>
        </div>
        <div className="text-xs">
          <p>reported on</p>
          <p className="text-xs">{dayjs(reportComment.createdAt).format('MM/D/YYYY')}</p>
        </div>
        <div className="text-xs">
          <p className="text-white bg-blue-400 p-1 rounded">status: {reportComment.status}</p>
          <button onClick={() => setIsModalOpen(true)} className="my-2 border-gray-600 rounded border py-1 px-2 w-full">
            Details
          </button>
        </div>
        {isModalOpen && (
          <DetailsModal handleCloseModal={handleCloseModal}>
            <FlaggedCommentModalDetails
              reportComment={reportComment}
              handleCloseModal={handleCloseModal}
              updateStatus={updateStatus}
            />
          </DetailsModal>
        )}
      </div>
    </div>
  );
};

export default FlaggedCommentListItem;
