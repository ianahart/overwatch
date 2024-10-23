import { ITopic } from '../../interfaces';
import CommunitySearchTopicListItem from './CommunitySearchTopicListItem';

export interface ICommunitySearchTopicListProps {
  topics: ITopic[];
}

const CommunitySearchTopicList = ({ topics }: ICommunitySearchTopicListProps) => {
  return (
    <div className="h-[250px] overflow-y-auto">
      {topics.map((topic) => {
        return <CommunitySearchTopicListItem key={topic.id} topic={topic} />;
      })}
    </div>
  );
};

export default CommunitySearchTopicList;
