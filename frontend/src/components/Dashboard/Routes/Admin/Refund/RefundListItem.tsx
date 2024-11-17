import dayjs from 'dayjs';
import { IRefund } from '../../../../../interfaces';
import { convertCentsToDollars } from '../../../../../util';
import { useState } from 'react';
import RefundModalDetails from './RefundModalDetails';
import DetailsModal from '../DetailsModal';

export interface IRefundListItemProps {
  refund: IRefund;
}

const RefundListItem = ({ refund }: IRefundListItemProps) => {
  const [isModalOpen, setIsModalOpen] = useState(false);

  const handleCloseModal = (): void => {
    setIsModalOpen(false);
  };

  return (
    <div className="p-2 rounded border border-gray-800 text-gray-400 relative">
      <div className="flex-wrap flex items-center justify-between">
        <div>
          <p>{refund.fullName}</p>
          <p className="text-xs">{dayjs(refund.createdAt).format('MM/D/YYYY')}</p>
        </div>
        <div className="mx-2">
          <p className="text-sm">
            Amount: ${convertCentsToDollars(refund.amount)}
            {refund.currency}
          </p>
        </div>
        <div className="mx-2">
          <p className="text-sm">
            Status: <span className="rounded bg-blue-400 text-white">{refund.status}</span>
          </p>
        </div>
        <div className="mx-2">
          <button
            onClick={() => setIsModalOpen(true)}
            className="border-gray-800 border rounded  p-1 hover:text-gray-600"
          >
            Refund Details
          </button>
        </div>
        {isModalOpen && (
          <DetailsModal handleCloseModal={handleCloseModal}>
            <RefundModalDetails refund={refund} handleCloseModal={handleCloseModal} />
          </DetailsModal>
        )}
      </div>
    </div>
  );
};

export default RefundListItem;
