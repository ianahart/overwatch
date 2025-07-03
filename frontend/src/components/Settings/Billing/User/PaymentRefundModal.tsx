import { AiOutlineClose } from 'react-icons/ai';
import { useSelector } from 'react-redux';
import { TRootState, useCreatePaymentRefundMutation } from '../../../../state/store';
import { useState } from 'react';
import { IError } from '../../../../interfaces';
import { nanoid } from 'nanoid';

export interface IPaymentRefundModalProps {
  handleCloseModal: () => void;
  stripePaymentIntentId: number;
  handleUpdateStatus: (id: number, newStatus: string) => void;
}

const PaymentRefundModal = ({
  handleCloseModal,
  stripePaymentIntentId,
  handleUpdateStatus,
}: IPaymentRefundModalProps) => {
  const MAX_CONTENT_LENGTH = 1000;
  const [reason, setReason] = useState('');
  const { user, token } = useSelector((store: TRootState) => store.user);
  const [errors, setErrors] = useState<string[]>([]);
  const [createPaymentRefund] = useCreatePaymentRefundMutation();

  const applyErrors = <T extends IError>(data: T): void => {
    for (let prop in data) {
      setErrors((prevState) => [...prevState, data[prop]]);
    }
  };

  const canSubmit = (): boolean => {
    if (reason.trim().length === 0 || reason.length > MAX_CONTENT_LENGTH) {
      const error = `Reason must be between 1 and ${MAX_CONTENT_LENGTH} characters`;
      setErrors((prevState) => [...prevState, error]);
      return false;
    }
    return true;
  };

  const handleOnSubmit = (e: React.FormEvent<HTMLFormElement>): void => {
    e.preventDefault();
    setErrors([]);
    if (!canSubmit) {
      return;
    }
    const payload = { reason, token, userId: user.id, stripePaymentIntentId };

    createPaymentRefund(payload)
      .unwrap()
      .then(() => {
        handleUpdateStatus(stripePaymentIntentId, 'PENDING');
        handleCloseModal();
      })
      .catch((err) => {
        applyErrors(err.data);
      });
  };

  return (
    <div data-testid="PaymentRefundModal" className="fixed inset-0 bg-gray-800 bg-opacity-90">
      <div className="flex items-center flex-col justify-center min-h-[60vh]">
        <div className="max-w-[600px] bg-gray-900 rounded shadow-lg p-2 w-full">
          <div className="m-2 flex justify-end">
            <div
              data-testid="refund-modal-close"
              onClick={handleCloseModal}
              className="bg-black p-2 rounded-3xl cursor-pointer"
            >
              <AiOutlineClose className="text-4xl text-white" />
            </div>
          </div>
          <form onSubmit={handleOnSubmit}>
            <div className="flex justify-center">
              {errors.map((error) => {
                return (
                  <p key={nanoid()} className="my-1 text-red-300 text-xs">
                    {error}
                  </p>
                );
              })}
            </div>
            <div className="flex flex-col my-4">
              <label htmlFor="reason">Reason for refund</label>
              <textarea
                className="border rounded border-gray-800 bg-transparent p-2 resize-none h-28"
                value={reason}
                onChange={(e) => setReason(e.target.value)}
                id="reason"
                name="reason"
              ></textarea>
            </div>
            <div className="my-4 flex justify-between">
              <button className="btn">Submit</button>
              <button className="outline-btn !text-gray-400" onClick={handleCloseModal}>
                Cancel
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
};

export default PaymentRefundModal;
