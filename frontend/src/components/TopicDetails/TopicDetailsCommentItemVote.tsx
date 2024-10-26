import { TbArrowBigUp, TbArrowBigDown } from 'react-icons/tb';

export interface ITopicDetailsCommentItemVoteProps {
  curUserHasVoted: boolean;
  curUserVoteType: string;
  voteDifference: number;
  createVote: (voteType: string) => void;
}

const TopicDetailsCommentItemVote = ({
  curUserHasVoted,
  curUserVoteType,
  voteDifference,
  createVote,
}: ITopicDetailsCommentItemVoteProps) => {
  const activeUpVote = curUserHasVoted && curUserVoteType === 'UPVOTE' ? 'text-blue-400' : 'text-gray-400';
  const activeDownVote = curUserHasVoted && curUserVoteType === 'DOWNVOTE' ? 'text-blue-400' : 'text-gray-400';
  return (
    <div className="m-2 flex items-center 2xl">
      <div onClick={() => createVote('UPVOTE')} className="mx-1 cursor-pointer">
        <TbArrowBigUp className={`text-xl ${activeUpVote}`} />
      </div>
      <div className="mx-1">
        <p>{voteDifference}</p>
      </div>
      <div onClick={() => createVote('DOWNVOTE')} className="mx-1 cursor-pointer">
        <TbArrowBigDown className={`text-xl ${activeDownVote}`} />
      </div>
    </div>
  );
};

export default TopicDetailsCommentItemVote;
