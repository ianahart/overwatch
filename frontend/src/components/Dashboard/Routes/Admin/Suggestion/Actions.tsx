import { useState } from 'react';
import { capitalize } from 'lodash';
import { useSelector } from 'react-redux';

import DetailsModal from '../DetailsModal';
import { TRootState, useDeleteSuggestionMutation, useUpdateSuggestionMutation } from '../../../../../state/store';

export interface IActionsProps {
  value: string;
  id: string;
  handleUpdateSuggestion: (feedbackStatus: string, id: string) => void;
  handleDeleteSuggestion: (id: string) => void;
}

const Actions = ({ value, id, handleUpdateSuggestion, handleDeleteSuggestion }: IActionsProps) => {
  const { token } = useSelector((store: TRootState) => store.user);
  const [updateSuggestion] = useUpdateSuggestionMutation();
  const [deleteSuggestion] = useDeleteSuggestionMutation();
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [status, setStatus] = useState(value);

  const handleOnChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    setStatus(e.target.value);
    const payload = { id: Number.parseInt(id), token, feedbackStatus: e.target.value };
    updateSuggestion(payload)
      .unwrap()
      .then((res) => {
        handleUpdateSuggestion(res.data, id);
      })
      .catch((err) => {
        console.log(err);
      });
  };

  const handleOnClick = () => {
    const payload = { id: Number.parseInt(id), token };
    deleteSuggestion(payload)
      .unwrap()
      .then(() => {
        handleDeleteSuggestion(id);
      })
      .catch((err) => {
        console.log(err);
      });
  };

  return (
    <div className="text-center">
      <p>{capitalize(value)}</p>
      <div>
        <button
          onClick={() => setIsModalOpen(true)}
          className="my-1 w-full text-green-400 cursor-pointer border border-gray-800 py-1 px-2 rounded"
        >
          Update
        </button>
        <button
          onClick={handleOnClick}
          className="my-1 w-full text-red-300 cursor-pointer border border-gray-800 py-1 px-2 rounded"
        >
          Delete
        </button>
      </div>
      {isModalOpen && (
        <DetailsModal handleCloseModal={() => setIsModalOpen(false)}>
          <form className="my-8">
            <p>
              You currently have this suggestion as <span className="font-bold">{capitalize(status)}</span>
            </p>
            <select
              className="bg-transparent w-full border border-gray-800 h-9"
              value={status}
              onChange={handleOnChange}
            >
              <option value="UNDER_REVIEW">Under Review</option>
              <option value="IMPLEMENTED">Implemented</option>
              <option value="REJECTED">Rejected</option>
              <option value="PENDING">Pending</option>
            </select>
          </form>
        </DetailsModal>
      )}
    </div>
  );
};

export default Actions;
