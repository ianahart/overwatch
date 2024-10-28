import { ITopic } from '../../interfaces';
import CommunityTopicListItem from './CommunityTopicListItem';

export interface ICommunitySearchTopicListProps {
  topics: ITopic[];
}

const CommunitySearchTopicList = ({ topics }: ICommunitySearchTopicListProps) => {
  return (
    <div className="h-[250px] overflow-y-auto">
      {topics.map((topic) => {
        return <CommunityTopicListItem key={topic.id} topic={topic} />;
      })}
    </div>
  );
};

export default CommunitySearchTopicList;
