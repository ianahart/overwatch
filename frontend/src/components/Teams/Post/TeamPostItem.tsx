import { Light as SyntaxHighlighter } from 'react-syntax-highlighter';
import { BsThreeDots, BsTrash } from 'react-icons/bs';
import dayjs from 'dayjs';
import { useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';

import tomorrowNight from 'react-syntax-highlighter/dist/esm/styles/hljs/tomorrow-night';
import Avatar from '../../Shared/Avatar';
import { ITeamComment, ITeamPost } from '../../../interfaces';
import {
  TRootState,
  removeTeamPost,
  useDeleteTeamPostMutation,
  useLazyFetchTeamCommentsQuery,
} from '../../../state/store';
import ClickAway from '../../Shared/ClickAway';
import TeamModal from '../TeamModal';
import TeamCommentForm from './Comment/TeamCommentForm';
import { paginationState } from '../../../data';
import TeamCommentList from './Comment/TeamCommentList';

export interface ITeamPostItemProps {
  teamPost: ITeamPost;
}

const decodeHtmlEntities = (input: string): string => {
  const doc = new DOMParser().parseFromString(input, 'text/html');
  return doc.documentElement.textContent || '';
};

const TeamPostItem = ({ teamPost }: ITeamPostItemProps) => {
  const dispatch = useDispatch();
  const [pag, setPag] = useState(paginationState);
  const [isClickAwayOpen, setIsClickAwayOpen] = useState(false);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [teamComments, setTeamComments] = useState<ITeamComment[]>([]);
  const decodedCode = decodeHtmlEntities(teamPost.code);
  const { token, user } = useSelector((store: TRootState) => store.user);
  const [deleteTeamPost] = useDeleteTeamPostMutation();
  const [fetchTeamComments] = useLazyFetchTeamCommentsQuery();

  const handleOnOpenModal = (): void => {
    setIsModalOpen(true);
  };

  const handleOnCloseModal = (): void => {
    setIsModalOpen(false);
  };

  const handleResetComments = () => {
    setTeamComments([]);
    setPag(paginationState);
  };

  const handleDeleteTeamPost = async (): Promise<void> => {
    try {
      await deleteTeamPost({ token, teamPostId: teamPost.id }).unwrap();
      dispatch(removeTeamPost(teamPost.id));
      setIsClickAwayOpen(false);
    } catch (err) {
      console.log(err);
    }
  };

  const paginateTeamComments = (dir: string, initial = false) => {
    const pageNumber = initial ? -1 : pag.page;
    fetchTeamComments({ page: pageNumber, direction: dir, pageSize: pag.pageSize, teamPostId: teamPost.id, token })
      .unwrap()
      .then((res) => {
        const { items, page, pageSize, totalPages, direction, totalElements } = res.data;
        setTeamComments((prevState) => [...prevState, ...items]);
        setPag((prevState) => ({
          ...prevState,
          page,
          pageSize,
          totalPages,
          totalElements,
          direction,
        }));
      })
      .catch((err) => {
        console.log(err);
      });
  };

  const updateTeamComment = (teamCommentId: number, content: string, tag: string): void => {
    setTeamComments(
      teamComments.map((teamComment) => {
        if (teamComment.id === teamCommentId) {
          return { ...teamComment, content, tag, isEdited: true };
        }
        return { ...teamComment };
      })
    );
  };

  return (
    <div className="my-6">
      <div className="flex justify-between">
        <div className="flex items-center">
          <Avatar initials="?.?" avatarUrl={teamPost.avatarUrl} height="h-9" width="w-9" />
          <div className="mb-2 ml-1">
            <h3>{teamPost.fullName}</h3>
            <p className="text-xs">{dayjs(teamPost.createdAt).format('MM/DD/YYYY')}</p>
          </div>
        </div>

        <div className="relative">
          <div onClick={() => setIsClickAwayOpen(true)}>
            <BsThreeDots />
          </div>

          {isClickAwayOpen && (
            <ClickAway onClickAway={() => setIsClickAwayOpen(false)}>
              <div className="border min-h-[40px] p-1 bg-black border-gray-800 rounded min-w-[120px] absolute top-0 left-0">
                {user.id === teamPost.userId ? (
                  <div onClick={handleDeleteTeamPost} className="cursor-pointer flex items-center">
                    <BsTrash className="text-xs mr-1" />
                    <p className="text-xs">Remove post</p>
                  </div>
                ) : (
                  <div>
                    <p className="text-xs">No actions available</p>
                  </div>
                )}
              </div>
            </ClickAway>
          )}
        </div>
      </div>
      <SyntaxHighlighter
        language={teamPost.language}
        style={tomorrowNight}
        customStyle={{
          backgroundColor: '#2e2e2e',
          borderRadius: '8px',
          padding: '16px',
          overflowX: 'auto',
          fontFamily: 'monospace',
          fontSize: '14px',
        }}
      >
        {decodedCode}
      </SyntaxHighlighter>
      <div className="flex items-center justify-between">
        <div>
          <button onClick={handleOnOpenModal} className="my-1 text-sm hover:text-gray-600">
            Leave a comment
          </button>
          {isModalOpen && (
            <TeamModal closeModal={handleOnCloseModal}>
              <TeamCommentForm
                updateTeamComment={() => {}}
                teamCommentId={0}
                handleResetComments={handleResetComments}
                teamPostId={teamPost.id}
                formType="create"
                closeModal={handleOnCloseModal}
              />
            </TeamModal>
          )}
        </div>

        {teamPost.hasComments && !teamComments.length && (
          <div>
            <button onClick={() => paginateTeamComments('next', true)} className="my-1 text-sm hover:text-gray-600">
              Read comments...
            </button>
          </div>
        )}
      </div>
      <TeamCommentList
        pag={pag}
        teamComments={teamComments}
        paginateTeamComments={paginateTeamComments}
        handleResetComments={handleResetComments}
        updateTeamComment={updateTeamComment}
      />
    </div>
  );
};

export default TeamPostItem;
