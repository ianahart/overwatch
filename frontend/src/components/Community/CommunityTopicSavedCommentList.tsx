import { useEffect, useState } from 'react';
import { IPaginationState, ISaveComment } from '../../interfaces';
import { useDeleteSaveCommentMutation, useLazyFetchSaveCommentsQuery } from '../../state/store';
import CommentHeader from '../Comment/CommentHeader';
import ToolTip from '../Shared/ToolTip';
import { FaBookmark } from 'react-icons/fa';

export interface ICommunityTopicSavedCommentListProps {
  userId: number;
  token: string;
}

const CommunityTopicSavedCommentList = ({ userId, token }: ICommunityTopicSavedCommentListProps) => {
  const pagState = {
    page: -1,
    pageSize: 10,
    totalPages: 0,
    direction: 'next',
    totalElements: 0,
  };
  const [pag, setPag] = useState<IPaginationState>(pagState);
  const [savedComments, setSavedComments] = useState<ISaveComment[]>([]);
  const [fetchSaveComments] = useLazyFetchSaveCommentsQuery();
  const [deleteSaveComment] = useDeleteSaveCommentMutation();

  useEffect(() => {
    paginateSavedComments('next');
  }, []);

  const paginateSavedComments = (dir: string) => {
    fetchSaveComments({ token, userId, page: pag.page, direction: dir, pageSize: pag.pageSize })
      .unwrap()
      .then((res) => {
        const { items, page, pageSize, totalPages, direction, totalElements } = res.data;
        setSavedComments(items);
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

  const handleDeleteSaveComment = (saveCommentId: number) => {
    const payload = { saveCommentId, token };
    deleteSaveComment(payload)
      .unwrap()
      .then(() => {
        setSavedComments((prevState) => prevState.filter((sc) => sc.id !== saveCommentId));
      })
      .catch((err) => {
        console.log(err);
      });
  };

  return (
    <div data-testid="saved-comment-list" className="my-4 max-w-[600px] w-full mx-auto">
      {savedComments.map((savedComment) => {
        return (
          <div data-testid="saved-comment" key={savedComment.id} className="border border-gray-800 p-2 rounded my-2">
            <CommentHeader comment={savedComment} />
            <div className="my-6">{savedComment.content}</div>
            <div className="flex justify-end">
              <ToolTip message="Unsave">
                <FaBookmark
                  data-testid="saved comment bookmark"
                  onClick={() => handleDeleteSaveComment(savedComment.id)}
                  className="cursor-pointer text-yellow-400"
                />
              </ToolTip>
            </div>
          </div>
        );
      })}
      <div className="flex items-center justify-center">
        {pag.page > 0 && (
          <button onClick={() => paginateSavedComments('prev')} className="mx-1">
            Prev
          </button>
        )}
        <p className="text-green-400">
          {pag.page + 1} of {pag.totalPages}
        </p>
        {pag.page < pag.totalPages - 1 && (
          <button onClick={() => paginateSavedComments('next')} className="mx-1">
            Next
          </button>
        )}
      </div>
    </div>
  );
};

export default CommunityTopicSavedCommentList;
