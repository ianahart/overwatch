import { useState } from 'react';
import { useSelector } from 'react-redux';

import { IError } from '../../interfaces';
import { TRootState, useCreateTeamMutation } from '../../state/store';
import { nanoid } from 'nanoid';

export interface ICreateTeamFormProps {
  closeModal: () => void;
}

const CreateTeamForm = ({ closeModal }: ICreateTeamFormProps) => {
  const { user, token } = useSelector((store: TRootState) => store.user);
  const [errors, setErrors] = useState<string[]>([]);
  const [teamName, setTeamName] = useState('');
  const [teamDescription, setTeamDescription] = useState('');
  const [createTeam] = useCreateTeamMutation();

  const handleOnChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>, element: string): void => {
    const value = e.target.value;
    if (element === 'input') {
      setTeamName(value);
      return;
    }
    if (element === 'textarea') {
      setTeamDescription(value);
    }
  };

  const canCreateTeam = (): boolean => {
    let isValid = true;
    const MAX_DESCRIPTION_LENGTH = 200;
    const MAX_NAME_LENGTH = 100;
    const isNameValidated = !(teamName.trim().length === 0 || teamName.length > MAX_NAME_LENGTH);
    const isDescValidated = !(teamDescription.trim().length === 0 || teamDescription.length > MAX_DESCRIPTION_LENGTH);

    if (!isNameValidated) {
      const error = `Name must be between 1 and ${MAX_NAME_LENGTH} characters`;
      setErrors((prevState) => [...prevState, error]);
      isValid = false;
    }

    if (!isDescValidated) {
      const error = `Description must be between 1 and ${MAX_DESCRIPTION_LENGTH} characters`;
      setErrors((prevState) => [...prevState, error]);
      isValid = false;
    }

    return isValid;
  };

  const applyErrors = <T extends IError>(res: T): void => {
    for (let prop in res) {
      setErrors((prevState) => [...prevState, res[prop]]);
    }
  };

  const handleOnSubmit = (e: React.FormEvent<HTMLFormElement>): void => {
    e.preventDefault();
    setErrors([]);

    if (!canCreateTeam()) {
      return;
    }

    const payload = { userId: user.id, token, teamName, teamDescription };

    createTeam(payload)
      .unwrap()
      .then(() => {
        closeModal();
      })
      .catch((err) => {
        applyErrors(err.data);
      });
  };

  return (
    <div data-testid="create-team-form">
      <div className="flex flex-col items-center">
        <h3 className="text-xl my-1">Create Team</h3>
        <p className="text-sm">Here you can create a team and invite other users to be apart of your team.</p>
        <p className="text-sm">You can message each other and share code snippets.</p>
      </div>
      <form onSubmit={handleOnSubmit} className="my-2">
        {errors.length > 0 && (
          <div className="flex flex-col">
            {errors.map((error) => {
              return (
                <p key={nanoid()} className="text-red-300 text-sm my-1">
                  {error}
                </p>
              );
            })}
          </div>
        )}
        <div className="flex flex-col my-8">
          <label htmlFor="name">Team name</label>
          <input
            onChange={(e) => handleOnChange(e, 'input')}
            value={teamName}
            id="name"
            name="name"
            placeholder="Enter team name..."
            className="h-9 border p-1 border-gray-800 bg-transparent rounded"
          />
        </div>
        <div className="flex flex-col my-8">
          <label htmlFor="description">Team description</label>
          <textarea
            onChange={(e) => handleOnChange(e, 'textarea')}
            value={teamDescription}
            id="description"
            name="description"
            placeholder="Enter team description..."
            className="h-20 p-1 border border-gray-800 bg-transparent rounded resize-none"
          />
        </div>
        <div className="my-8 flex justify-center">
          <button type="submit" className="mx-4 btn">
            Create
          </button>
          <button onClick={closeModal} type="button" className="mx-4 outline-btn !text-gray-400 border border-gray-800">
            Cancel
          </button>
        </div>
      </form>
    </div>
  );
};

export default CreateTeamForm;
