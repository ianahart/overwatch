import { useParams, useSearchParams } from 'react-router-dom';
import { useSelector } from 'react-redux';
import { useEffect, useState } from 'react';

import { TRootState, useFetchCommentQuery, useLazyFetchReplyCommentsByUserQuery } from '../../state/store';
import { minCommentState } from '../../data';
import { IMinComment, IReplyComment } from '../../interfaces';
import OriginalComment from './OriginalComment';
import CommentList from './CommentList';

const ReplyComment = () => {
  const paginationState = {
    page: 0,
    pageSize: 10,
    totalPages: 0,
    direction: 'next',
    totalElements: 0,
  };

  let { commentId } = useParams();
  const parsedCommentId = commentId ? Number.parseInt(commentId) : 0;
  const [params] = useSearchParams();
  let senderId = params.get('sender');
  const parsedSenderId = senderId ? Number.parseInt(senderId) : 0;
  const [fetchReplyCommentsByUser] = useLazyFetchReplyCommentsByUserQuery();
  const { token } = useSelector((store: TRootState) => store.user);
  const { data } = useFetchCommentQuery({ token, commentId: parsedCommentId });
  const [comment, setComment] = useState<IMinComment>(minCommentState);
  const [replyComments, setReplyComments] = useState<IReplyComment[]>([]);
  const [pag, setPag] = useState(paginationState);

  useEffect(() => {
    if (data !== undefined) {
      setComment(data.data);
    }
  }, [data]);

  const fetchReplyComments = (dir: string): void => {
    const payload = {
      token,
      commentId: comment.id,
      userId: parsedSenderId,
      direction: dir,
      page: pag.page,
      pageSize: pag.pageSize,
    };
    fetchReplyCommentsByUser(payload)
      .unwrap()
      .then((res) => {
        const { items, totalPages, totalElements, pageSize, page, direction } = res.data;

        setPag((prevState) => ({
          ...prevState,
          page,
          pageSize,
          totalPages,
          direction,
          totalElements,
        }));

        setReplyComments((prevState) => [...prevState, ...items]);
      })
      .catch((err) => {
        console.log(err);
      });
  };

  useEffect(() => {
    if (comment.id !== 0) {
      fetchReplyComments('next');
    }
  }, [comment.id]);

  return (
    <div className="min-h-[100vh] max-w-[1280px]">
      <div className="max-w-[600px] mx-auto w-full flex flex-col items-center">
        <OriginalComment comment={comment} />
        <div className="my-4 w-full">
          <div className="flex justify-center">
            <h3 className="text-xl text-gray-400">Replies</h3>
          </div>
          {comment.id !== 0 && (
            <div className="my-2">
              <CommentList replyComments={replyComments} />
            </div>
          )}

          {pag.page < pag.totalPages - 1 && (
            <div className="my-2 flex justify-center">
              <button onClick={() => fetchReplyComments('next')}>Load more...</button>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default ReplyComment;
