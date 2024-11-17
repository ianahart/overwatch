import FlaggedCommentList from './FlaggedCommentList';
import FlaggedCommentTitle from './FlaggedCommentTitle';

const FlaggedComment = () => {
  return (
    <div className="md:max-w-[1450px] w-full mx-auto mt-8">
      <div className="bg-gray-900 p-2 rounded">
        <FlaggedCommentTitle />
        <FlaggedCommentList />
      </div>
    </div>
  );
};
export default FlaggedComment;
