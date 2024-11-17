import { AiOutlineClose } from 'react-icons/ai';
import { useState } from 'react';
import { nanoid } from 'nanoid';
import { useSelector } from 'react-redux';

import { IError, IRefund } from '../../../../../interfaces';
import { TRootState, useUpdatePaymentRefundMutation } from '../../../../../state/store';

export interface IRefundModalProps {
  refund: IRefund;
  handleCloseModal: () => void;
}

const RefundModal = ({ refund, handleCloseModal }: IRefundModalProps) => {
  const { user, token } = useSelector((store: TRootState) => store.user);
  const MAX_CONTENT_LENGTH = 300;
  const [notes, setNotes] = useState('');
  const [errors, setErrors] = useState<string[]>([]);
  const [updatePaymentRefund] = useUpdatePaymentRefundMutation();
  const [approvalStatus, setApprovalStatus] = useState<string | null>(null);

  const applyErrors = <T extends IError>(data: T): void => {
    for (let prop in data) {
      setErrors((prevState) => [...prevState, data[prop]]);
    }
  };

  const handleOptionChange = (e: React.ChangeEvent<HTMLInputElement>): void => {
    setApprovalStatus(e.target.value);
  };

  const canSubmitRefund = (): boolean => {
    if (notes.trim().length === 0 || notes.length > MAX_CONTENT_LENGTH) {
      const error = `Admin notes must be between 1 and ${MAX_CONTENT_LENGTH} characters`;
      setErrors((prevState) => [...prevState, error]);
      return false;
    }
    return true;
  };

  const handleOnSubmit = (e: React.FormEvent<HTMLFormElement>): void => {
    e.preventDefault();
    setErrors([]);

    if (!canSubmitRefund() && approvalStatus !== null) {
      return;
    }

    const payload = {
      userId: user.id,
      token,
      adminNotes: notes,
      stripePaymentIntentId: refund.stripePaymentIntentId,
      id: refund.id,
      status: approvalStatus as string,
    };
    updatePaymentRefund(payload)
      .unwrap()
      .then((res) => {
        console.log(res);
        handleCloseModal();
      })
      .catch((err) => {
        console.log(err);
        applyErrors(err.data);
      });
  };
  return (
    <div className="fixed inset-0 bg-gray-800 bg-opacity-90">
      <div className="flex items-center flex-col justify-center min-h-[60vh]">
        <div className="max-w-[600px] bg-gray-900 rounded shadow-lg p-2 w-full">
          <div className="m-2 flex justify-end">
            <div onClick={handleCloseModal} className="bg-black p-2 rounded-3xl cursor-pointer">
              <AiOutlineClose className="text-4xl text-white" />
            </div>
          </div>
          <div className="my-4">
            <h3 className="text-lg">Reason for refund</h3>
            <p>From: {refund.fullName}</p>
            <div className="h-24 overflow-y-auto">
              <p>{refund.reason}</p>
            </div>
          </div>
          <div className="my-4">
            <h3 className="text-lg text-center">Complete Refund</h3>
            {errors.length > 0 && (
              <div className="my-4 flex flex-col items-center">
                {errors.map((error) => {
                  return (
                    <p key={nanoid()} className="text-xs text-red-300">
                      {error}
                    </p>
                  );
                })}
              </div>
            )}
            <form onSubmit={handleOnSubmit}>
              <div className="flex flex-col">
                <label htmlFor={`notes${refund.id}`}>Admin Notes</label>
                <textarea
                  onChange={(e) => setNotes(e.target.value)}
                  value={notes}
                  name={`notes${refund.id}`}
                  id={`notes${refund.id}`}
                  className="h-24 bg-transparent resize-none border border-gray-800 rounded"
                ></textarea>
              </div>
              <div className="flex items-center justify-center gap-4 my-4">
                <label className="flex items-center gap-2">
                  <input
                    type="radio"
                    value="approve"
                    checked={approvalStatus === 'approve'}
                    onChange={handleOptionChange}
                    className="mr-1"
                  />
                  Approve
                </label>

                <label className="flex items-center gap-2">
                  <input
                    type="radio"
                    value="reject"
                    checked={approvalStatus === 'reject'}
                    onChange={handleOptionChange}
                    className="mr-1"
                  />
                  Reject
                </label>
              </div>

              <div className="my-4 flex justify-between">
                <button type="submit" className="btn">
                  Refund
                </button>
                <button type="button" onClick={handleCloseModal}>
                  Cancel
                </button>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
  );
};

export default RefundModal;
