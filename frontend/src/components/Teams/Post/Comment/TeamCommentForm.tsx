import { useEffect, useState } from 'react';
import { IError } from '../../../../interfaces';
import {
  TRootState,
  useCreateTeamCommentMutation,
  useFetchTeamCommentQuery,
  useUpdateTeamCommentMutation,
} from '../../../../state/store';
import { useSelector } from 'react-redux';

export interface ITeamCommentFormProps {
  teamCommentId: number;
  teamPostId: number;
  formType: string;
  closeModal: () => void;
  handleResetComments: () => void;
  updateTeamComment: (teamCommentId: number, content: string) => void;
}

const TeamCommentForm = ({
  teamCommentId,
  teamPostId,
  formType,
  closeModal,
  handleResetComments,
  updateTeamComment,
}: ITeamCommentFormProps) => {
  const MAX_COMMENT_LENGTH = 200;
  const { user, token } = useSelector((store: TRootState) => store.user);
  const [error, setError] = useState('');
  const [content, setContent] = useState('');
  const { data } = useFetchTeamCommentQuery({ token, teamCommentId, teamPostId });
  const [createTeamCommentMutation] = useCreateTeamCommentMutation();
  const [updateTeamCommentMutation] = useUpdateTeamCommentMutation();

  useEffect(() => {
    if (token && data !== undefined) {
      console.log(data);
      setContent(data.data);
    }
  }, [data]);

  const handleOnChange = (e: React.ChangeEvent<HTMLTextAreaElement>): void => {
    setContent(e.target.value);
  };

  const applyErrors = <T extends IError>(data: T) => {
    for (let prop in data) {
      setError(data[prop]);
    }
  };

  const canSubmitForm = (): boolean => {
    if (content.trim().length === 0 || content.length > MAX_COMMENT_LENGTH) {
      const validationError = `Comment must be between 1 and ${MAX_COMMENT_LENGTH} characters`;
      setError(validationError);
      return false;
    }
    return true;
  };

  const handleCreateComment = (): void => {
    const payload = { userId: user.id, content, token, teamPostId };
    createTeamCommentMutation(payload)
      .unwrap()
      .then(() => {
        closeModal();
        handleResetComments();
      })
      .catch((err) => {
        applyErrors(err.data);
        console.log(err);
      });
  };

  const handleUpdateComment = (): void => {
    const payload = { teamCommentId, content, token, teamPostId };
    updateTeamCommentMutation(payload)
      .unwrap()
      .then((res) => {
        updateTeamComment(teamCommentId, res.data);
        closeModal();
      })
      .catch((err) => {
        console.log(err);
      });

    console.log('editing comment');
  };

  const handleOnSubmit = (e: React.FormEvent<HTMLFormElement>): void => {
    e.preventDefault();

    if (!canSubmitForm()) {
      return;
    }

    if (formType === 'create') {
      handleCreateComment();
      return;
    }

    if (formType === 'edit') {
      handleUpdateComment();
    }
  };

  return (
    <div>
      <form onSubmit={handleOnSubmit}>
        {error.length > 0 && (
          <div className="my-4">
            <p className="text-sm text-red-300">{error}</p>
          </div>
        )}
        <div className="my-4 flex flex-col">
          <label htmlFor="content" className="mb-1">
            {formType === 'create' ? 'Create' : 'Edit'} Comment
          </label>
          <textarea
            onChange={handleOnChange}
            value={content}
            id="contet"
            name="content"
            className="bg-transparent border border-gray-700 rounded resize-none h-20 p-1"
          ></textarea>
        </div>
        <div className="my-4 flex justify-between">
          <button className="mx-2 btn" type="submit">
            {formType === 'create' ? 'Create' : 'Update'}
          </button>
          <button
            className="mx-2 text-gray-400 h-9 border-gray-800 border rounded p-2"
            onClick={closeModal}
            type="button"
          >
            Cancel
          </button>
        </div>
      </form>
    </div>
  );
};

export default TeamCommentForm;
