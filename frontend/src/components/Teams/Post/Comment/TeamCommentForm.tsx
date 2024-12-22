import { useCallback, useEffect, useRef, useState } from 'react';
import { useSelector } from 'react-redux';
import { debounce } from 'lodash';
import { useParams } from 'react-router-dom';

import { IError, ITeamMember } from '../../../../interfaces';
import {
  TRootState,
  useCreateTeamCommentMutation,
  useFetchTeamCommentQuery,
  useLazySearchTeamMembersQuery,
  useUpdateTeamCommentMutation,
} from '../../../../state/store';
import { sendMessage } from '../../../../util/WebSocketService';
import { NotificationType } from '../../../../enums';
import ClickAway from '../../../Shared/ClickAway';
import { paginationState, teamMember as teamMemberState } from '../../../../data';
import Avatar from '../../../Shared/Avatar';
import { initializeName } from '../../../../util';

export interface ITeamCommentFormProps {
  teamCommentId: number;
  teamPostId: number;
  formType: string;
  closeModal: () => void;
  handleResetComments: () => void;
  updateTeamComment: (teamCommentId: number, content: string, tag: string) => void;
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
  const inputRef = useRef<HTMLInputElement | null>(null);
  const params = useParams();
  const teamId = Number.parseInt(params.teamId as string);
  const { user, token } = useSelector((store: TRootState) => store.user);
  const [pag, setPag] = useState(paginationState);
  const [selectedTeamMember, setSelectedTeamMember] = useState<ITeamMember>(teamMemberState);
  const [teamMembers, setTeamMembers] = useState<ITeamMember[]>([]);
  const [error, setError] = useState('');
  const [content, setContent] = useState('');
  const [tag, setTag] = useState('');
  const [isTagOpen, setIsTagOpen] = useState(false);
  const { data } = useFetchTeamCommentQuery(
    { token, teamCommentId, teamPostId },
    { skip: !token || !teamCommentId || !teamPostId }
  );
  const [createTeamCommentMutation] = useCreateTeamCommentMutation();
  const [updateTeamCommentMutation] = useUpdateTeamCommentMutation();
  const [searchForTeamMembersQuery] = useLazySearchTeamMembersQuery();

  useEffect(() => {
    if (token && data !== undefined) {
      setContent(data.data.content);
      setTag(data.data.tag);
    }
  }, [data]);

  useEffect(() => {
    if (isTagOpen) {
      inputRef.current?.focus();
    }
  }, [isTagOpen]);

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
    const payload = { userId: user.id, content, token, teamPostId, tag: selectedTeamMember.fullName };
    createTeamCommentMutation(payload)
      .unwrap()
      .then(() => {
        if (selectedTeamMember.id !== 0) {
          emitNotification();
        }
        closeModal();
        handleResetComments();
      })
      .catch((err) => {
        applyErrors(err.data);
        console.log(err);
      });
  };

  const emitNotification = (): void => {
    const payload = {
      receiverId: selectedTeamMember.userId,
      senderId: user.id,
      notificationType: NotificationType.TAG,
    };
    sendMessage('/api/v1/notify', JSON.stringify(payload));
  };

  const handleUpdateComment = (): void => {
    const payload = { teamCommentId, content, token, teamPostId, tag: selectedTeamMember.fullName };
    updateTeamCommentMutation(payload)
      .unwrap()
      .then((res) => {
        updateTeamComment(teamCommentId, res.data.content, res.data.tag);
        if (selectedTeamMember.id !== 0) {
          emitNotification();
        }
        closeModal();
      })
      .catch((err) => {
        console.log(err);
      });
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

  const handleOnKeyDown = (e: React.KeyboardEvent<HTMLTextAreaElement>) => {
    if (e.key === '@' && teamId !== 0) {
      setIsTagOpen(true);
    } else {
      setIsTagOpen(false);
    }
  };

  const handleOnClickAway = () => {
    setIsTagOpen(false);
    setTag('');
    setTeamMembers([]);
    setSelectedTeamMember(teamMemberState);
    setPag(paginationState);
  };

  const searchForTeamMembers = (query: string, paginate: boolean) => {
    const pageNum = paginate ? pag.page : -1;
    if (query.trim().length === 0) return;
    searchForTeamMembersQuery({
      token,
      search: query,
      page: pageNum,
      pageSize: 1,
      direction: pag.direction,
      teamId,
    })
      .unwrap()
      .then((res) => {
        const { items, page, pageSize, totalPages, direction, totalElements } = res.data;
        setPag((prevState) => ({
          ...prevState,
          page,
          pageSize,
          totalElements,
          totalPages,
          direction,
        }));
        if (paginate) {
          setTeamMembers((prevState) => [...prevState, ...items]);
        } else {
          setTeamMembers(items);
        }
      })
      .catch((err) => console.log(err));
  };

  const debouncedSearchForTeamMembers = useCallback(
    debounce((query: string) => {
      searchForTeamMembers(query, false);
    }, 300),
    []
  );

  const handleOnTagChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const currentTag = e.target.value.replace('@', '');
    setTag(currentTag);
    debouncedSearchForTeamMembers(currentTag);
  };

  const handleSelectTeamMember = (teamMember: ITeamMember) => {
    setSelectedTeamMember(teamMember);
    setIsTagOpen(false);
    setTag('');
    setTeamMembers([]);
    setPag(paginationState);
  };

  return (
    <div>
      <form onSubmit={handleOnSubmit}>
        {error.length > 0 && (
          <div className="my-4">
            <p className="text-sm text-red-300">{error}</p>
          </div>
        )}
        <div className="my-4 flex flex-col relative">
          <label htmlFor="content" className="mb-1">
            {formType === 'create' ? 'Create' : 'Edit'} Comment
          </label>
          <small>To tag a team member type &quot;@&quot;</small>
          {selectedTeamMember.fullName.length > 0 && <small>Tagged {selectedTeamMember.fullName}</small>}
          <textarea
            onKeyDown={handleOnKeyDown}
            onChange={handleOnChange}
            value={content}
            id="contet"
            name="content"
            className="bg-transparent border border-gray-700 rounded resize-none h-20 p-1"
          ></textarea>
          {isTagOpen && (
            <ClickAway onClickAway={handleOnClickAway}>
              <div className="absolute z-10 top-8 left-10">
                <input
                  placeholder="Enter team member"
                  onChange={handleOnTagChange}
                  value={tag}
                  className="border border-gray-800 bg-gray-800  rounded h-9"
                  ref={inputRef}
                />
                {teamMembers.length > 0 && (
                  <div className="bg-gray-800 shadow-lg p-2">
                    {teamMembers.map((teamMember) => {
                      return (
                        <div
                          onClick={() => handleSelectTeamMember(teamMember)}
                          key={teamMember.id}
                          className="my-2 flex items-center rounded hover:bg-gray-700 cursor-pointer"
                        >
                          <div>
                            <Avatar
                              width="w-6"
                              height="h-6"
                              initials={initializeName(
                                teamMember.fullName.split(' ')[0],
                                teamMember.fullName.split(' ')[1]
                              )}
                              avatarUrl={teamMember.avatarUrl}
                            />
                          </div>
                          <div>
                            <p className="text-xs">{teamMember.fullName}</p>
                          </div>
                        </div>
                      );
                    })}
                    {pag.page < pag.totalPages - 1 && (
                      <button onClick={() => searchForTeamMembers(tag, true)} className="text-xs">
                        Load more...
                      </button>
                    )}
                  </div>
                )}
              </div>
            </ClickAway>
          )}
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
