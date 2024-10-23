import { Link } from 'react-router-dom';

export interface ICommunityCreateTopicLinkProps {
  linkText: string;
}

const CommunityCreateTopicLink = ({ linkText }: ICommunityCreateTopicLinkProps) => {
  return (
    <div className="flex justify-center">
      <Link className="bg-blue-400 text-black rounded-lg p-2 cursor-pointer" to="/community/create-topic">
        {linkText}
      </Link>
    </div>
  );
};

export default CommunityCreateTopicLink;
