import { useNavigate } from 'react-router-dom';
import { ITopic } from '../../interfaces';
import { shortenString } from '../../util';

export interface ICommunityTopicListItemProps {
  topic: ITopic;
}

const CommunityTopicListItem = ({ topic }: ICommunityTopicListItemProps) => {
  const navigate = useNavigate();
  const goToTopicPage = () => {
    navigate(`/community/topics/${topic.id}`);
  };

  return (
    <div className="my-4 cursor-pointer" onClick={goToTopicPage}>
      <h3 className="text-xl">{topic.title}</h3>
      <p className="text-sm">{shortenString(topic.description, 10)}</p>
      <div className="flex flex-wrap my-2">
        {topic.tags.map((tag) => {
          return (
            <div key={tag.id} className="mx-2 p-2 rounded-lg bg-blue-400">
              <p className="text-xs text-black">#{tag.name}</p>
            </div>
          );
        })}
      </div>
    </div>
  );
};

export default CommunityTopicListItem;
