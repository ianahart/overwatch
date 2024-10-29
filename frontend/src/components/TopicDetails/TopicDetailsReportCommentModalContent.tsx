import { useState } from 'react';
import { useSelector } from 'react-redux';

import { IError } from '../../interfaces';
import { TRootState, useCreateReportCommentMutation } from '../../state/store';

export interface ITopicDetailsReportCommentModalContentProps {
  currentUserId: number;
  commentId: number;
  content: string;
  closeModal: () => void;
}

const TopicDetailsReportCommentModalContent = ({
  currentUserId,
  commentId,
  content,
  closeModal,
}: ITopicDetailsReportCommentModalContentProps) => {
  const { token } = useSelector((store: TRootState) => store.user);
  const MAX_DETAILS_LENGTH = 400;
  const [reason, setReason] = useState('');
  const [details, setDetails] = useState('');
  const [error, setError] = useState('');
  const [reportCommentMut] = useCreateReportCommentMutation();
  const reasons = [
    { id: 1, name: 'Spam', value: 'SPAM' },
    { id: 2, name: 'Harassment', value: 'HARASSMENT' },
    { id: 3, name: 'Hate Speech', value: 'HATE_SPEECH' },
    { id: 4, name: 'MisInformation', value: 'MISINFORMATION' },
    { id: 5, name: 'Other', value: 'OTHER' },
  ];

  const applyServerErrors = <T extends IError>(errors: T): void => {
    for (let prop in errors) {
      setError(errors[prop]);
    }
  };

  const handleOnSubmit = (e: React.FormEvent<HTMLFormElement>): void => {
    e.preventDefault();
    if (details.trim().length === 0 || details.length > MAX_DETAILS_LENGTH) {
      setError(`Details must be between 1 and ${MAX_DETAILS_LENGTH}`);
      return;
    }

    if (reason.length === 0) {
      setError('Please select a reason why you are reporting this comment');
      return;
    }

    reportComment();
  };

  const reportComment = () => {
    const payload = { userId: currentUserId, token, commentId, details, reason };

    reportCommentMut(payload)
      .unwrap()
      .then(() => {
        closeModal();
      })
      .catch((err) => {
        console.log(err);
        applyServerErrors(err.data);
      });
  };

  const handleOnClick = (e: React.MouseEvent<HTMLButtonElement>) => {
    e.stopPropagation();
    closeModal();
  };

  return (
    <>
      <div className="flex justify-center my-2">
        <h2 className="text-gray-400 text-2xl">Report this comment</h2>
      </div>
      <div className="my-2">
        <p className="text-gray-400 mb-1">Comment under question:</p>
        <div className="p-2 bg-gray-950 rounded">
          <p>{content}</p>
        </div>
      </div>
      <div className="my-8">
        <form onSubmit={handleOnSubmit}>
          <div className="flex-col flex my-8">
            <label htmlFor="reason">Reason for reporting</label>
            <select
              id="reason"
              name="reason"
              value={reason}
              onChange={(e) => setReason(e.target.value)}
              className="bg-transparent p-2 h-9 border border-gray-800"
            >
              {reasons.map(({ id, value, name }) => {
                return (
                  <option key={id} value={value}>
                    {name}
                  </option>
                );
              })}
            </select>
          </div>
          <div className="flex-col flex my-8">
            <label htmlFor="details">Additional details</label>
            <textarea
              id="details"
              name="details"
              value={details}
              onChange={(e) => setDetails(e.target.value)}
              placeholder="Details on the reason you're reporting for..."
              className="h-20 bg-transparent p-2 rounded border border-gray-800 resize-none"
            ></textarea>
          </div>
          {error.length > 0 && (
            <div className="my-2 flex justify-center">
              <p className="text-sm text-red-300">{error}</p>
            </div>
          )}
          <div className="my-4 flex">
            <button type="submit" className="btn mx-2">
              Report
            </button>
            <button onClick={handleOnClick} type="button" className="outline-btn mx-2 !bg-gray-400">
              Cancel
            </button>
          </div>
        </form>
      </div>
    </>
  );
};

export default TopicDetailsReportCommentModalContent;
