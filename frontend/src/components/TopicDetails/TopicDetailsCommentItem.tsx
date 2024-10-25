import dayjs from 'dayjs';
import { TbArrowBigDown, TbArrowBigUp } from 'react-icons/tb';
import { useSelector } from 'react-redux';
import { useNavigate } from 'react-router-dom';

import { IComment } from '../../interfaces';
import Avatar from '../Shared/Avatar';
import { initializeName } from '../../util';
import { TRootState, useCreateVoteMutation } from '../../state/store';

export interface ITopicDetailsCommentItemProps {
  comment: IComment;
}

const TopicDetailsCommentItem = ({ comment }: ITopicDetailsCommentItemProps) => {
  const navigate = useNavigate();
  const { user, token } = useSelector((store: TRootState) => store.user);
  const [createVoteMut] = useCreateVoteMutation();
  const [firstName, lastName] = comment.fullName.split(' ');

  const createVote = (voteType: string) => {
    if (user.id === 0) {
      navigate('/signin');
      return;
    }

    const payload = { userId: user.id, commentId: comment.id, token, voteType };
    createVoteMut(payload)
      .unwrap()
      .then((res) => {
        console.log(res);
      })
      .catch((err) => {
        console.log(err);
      });
  };

  return (
    <div className="border my-4 p-2 rounded-lg border-gray-800">
      <div className="my-2">
        <div className="flex items-center">
          <Avatar
            height="h-9"
            width="w-9"
            avatarUrl={comment.avatarUrl}
            initials={initializeName(firstName, lastName)}
          />
          <div className="ml-2">
            <h3>{comment.fullName}</h3>
            <p className="text-xs">{dayjs(comment.createdAt).format('MM/DD/YYYY')}</p>
          </div>
        </div>
        <div className="my-2">
          <p>{comment.content}</p>
        </div>
        <div className="my-2">
          <div className="m-2 flex items-center 2xl">
            <div onClick={() => createVote('UPVOTE')} className="mx-1 cursor-pointer">
              <TbArrowBigUp className="text-xl" />
            </div>
            <div className="mx-1">
              <p>0</p>
            </div>
            <div onClick={() => createVote('DOWNVOTE')} className="mx-1 cursor-pointer">
              <TbArrowBigDown className="text-xl" />
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default TopicDetailsCommentItem;
