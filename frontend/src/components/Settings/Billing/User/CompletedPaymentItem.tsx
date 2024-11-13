import dayjs from 'dayjs';
import { IPaymentIntent } from '../../../../interfaces';
import { convertCentsToDollars, initializeName } from '../../../../util';
import Avatar from '../../../Shared/Avatar';
import { useState } from 'react';
import PaymentRefundModal from './PaymentRefundModal';

export interface ICompletedPaymentItemProps {
  paymentIntent: IPaymentIntent;
  handleUpdateStatus: (id: number, newStatus: string) => void;
}

const CompletedPaymentItem = ({ paymentIntent, handleUpdateStatus }: ICompletedPaymentItemProps) => {
  const [first, last] = paymentIntent.fullName.split(' ');
  const [isModalOpen, setIsModalOpen] = useState(false);

  const handleCloseModal = (): void => {
    setIsModalOpen(false);
  };

  return (
    <div className="my-4 flex justify-between">
      <div className="flex items-center">
        <Avatar initials={initializeName(first, last)} width="w-9" height="h-9" avatarUrl={paymentIntent.avatarUrl} />
        <div className="ml-2">
          <p className="text-sm">{paymentIntent.fullName}</p>
          <p className="text-xs">
            Paid on: <span className="text-xs font-bold">{dayjs(paymentIntent.createdAt).format('MM/D/YYYY')}</span>
          </p>
        </div>
      </div>
      <div className="flex items-center">
        <p className="text-xs mx-2">
          Status: <span className="font-bold text-gray-4000 ml-2">{paymentIntent.status}</span>
        </p>
        <p className="mx-2">
          <span className="text-red-300 text-xs">$-{convertCentsToDollars(paymentIntent.amount)}</span>
          <span className="text-xs ml-2">{paymentIntent.currency}</span>
        </p>
        <div className="mx-2">
          <button
            onClick={() => setIsModalOpen(true)}
            className="text-xs text-gray-400 border p-2 rounded border-gray-800"
          >
            Ask for refund
          </button>
        </div>
      </div>
      {isModalOpen && (
        <PaymentRefundModal
          stripePaymentIntentId={paymentIntent.id}
          handleCloseModal={handleCloseModal}
          handleUpdateStatus={handleUpdateStatus}
        />
      )}
    </div>
  );
};

export default CompletedPaymentItem;
