import { useState } from 'react';
import { useSelector } from 'react-redux';

import { ITeam } from '../../../../interfaces';
import {
  TRootState,
  useCreateTeamPinnedMessageMutation,
  useUpdateTeamPinnedMessageMutation,
} from '../../../../state/store';
import { nanoid } from 'nanoid';

export interface IMessageFormProps {
  message: string;
  formType: string;
  team: ITeam;
  closeModal: () => void;
  teamPinnedMessageId?: number;
}

const MessageForm = ({
  formType,
  team,
  closeModal,
  message: messageState,
  teamPinnedMessageId = 0,
}: IMessageFormProps) => {
  const { token } = useSelector((store: TRootState) => store.user);
  const MAX_TEXTAREA_LENGTH = 100;
  const [message, setMessage] = useState(messageState);
  const [error, setError] = useState('');
  const [serverErrors, setServerErrors] = useState<string[]>([]);
  const [createMessageMut] = useCreateTeamPinnedMessageMutation();
  const [updateMessageMut] = useUpdateTeamPinnedMessageMutation();

  const clearErrors = (): void => {
    setError('');
    setServerErrors([]);
  };

  const errorsPresent = (message: string): boolean => {
    if (message.trim().length === 0 || message.length > MAX_TEXTAREA_LENGTH) {
      setError(`Message must be between 1 and ${MAX_TEXTAREA_LENGTH} characters`);
      return true;
    }
    return false;
  };

  const applyServerErrors = <T extends object>(data: T) => {
    for (const [key, val] of Object.entries(data)) {
      if (key === 'message') {
        setServerErrors((prevState) => [...prevState, val]);
      }
    }
  };

  const handleOnSubmit = (e: React.FormEvent<HTMLFormElement>): void => {
    e.preventDefault();
    clearErrors();
    if (errorsPresent(message)) {
      return;
    }
    if (formType === 'update') {
      updateMessage(team, message);
    } else if (formType === 'create') {
      createMessage(team, message);
    }
  };

  const updateMessage = (team: ITeam, message: string): void => {
    const payload = { teamId: team.id, userId: team.userId, message, token, teamPinnedMessageId };
    updateMessageMut(payload)
      .unwrap()
      .then(() => {
        closeModal();
      })
      .catch((err) => {
        applyServerErrors(err.data);
      });
  };

  const createMessage = (team: ITeam, message: string): void => {
    const payload = { teamId: team.id, userId: team.userId, message, token };
    createMessageMut(payload)
      .unwrap()
      .then(() => {
        closeModal();
      })
      .catch((err) => {
        applyServerErrors(err.data);
      });
  };

  const handleOnChange = (e: React.ChangeEvent<HTMLTextAreaElement>): void => {
    setMessage(e.target.value);
  };

  return (
    <form data-testid="message-form" onSubmit={handleOnSubmit}>
      <div className="flex flex-col w-full">
        <div className="my-4">
          <h3 className="text-xl">{formType === 'update' ? 'Update Message' : 'Create New Message'}</h3>
          <p>This message will be pinned and saved on the team members page until it is deleted by the admin.</p>
          {error.length > 0 && <p className="text-sm text-red-400">{error}</p>}
          {serverErrors.map((serverError) => {
            return (
              <p className="text-sm text-red-400" key={nanoid()}>
                {serverError}
              </p>
            );
          })}
        </div>
        <div className="my-4">
          <textarea
            onChange={handleOnChange}
            value={message}
            className="w-full resize-none border border-gray-800 bg-transparent rounded-sm min-h-20"
          ></textarea>
        </div>
        <div className="my-4">
          <button type="submit" className="btn w-full my-1">
            {formType === 'update' ? 'Update' : 'Create'}
          </button>
          <button onClick={closeModal} type="submit" className="btn !text-black !bg-gray-600 w-full my-1">
            Cancel
          </button>
        </div>
      </div>
    </form>
  );
};

export default MessageForm;
