import { useParams } from 'react-router-dom';
import { useEffect, useState } from 'react';
import { useFetchTopicQuery } from '../../state/store';
import { topicState } from '../../data';
import { ITopic } from '../../interfaces';
import Spinner from '../Shared/Spinner';
import TopicDetailsHeader from './TopicDetailsHeader';
import TopicDetailsDescription from './TopicDetailsDescription';
import TopicDetailsStats from './TopicDetailsStats';
import TopicDetailsCommentModal from './TopicDetailsCommentModal';
import TopicDetailsComments from './TopicDetailsComments';

const TopicDetails = () => {
  let { topicId: potentialTopicId } = useParams();
  const topicId = Number.parseInt(potentialTopicId as string);
  const { data, isLoading } = useFetchTopicQuery({ topicId });
  const [topic, setTopic] = useState<ITopic>(topicState);

  useEffect(() => {
    if (data !== undefined) {
      setTopic(data.data);
    }
  }, [data]);

  return (
    <>
      <div className="max-w-[1280px] mx-auto p-2 min-h-[100vh]">
        {isLoading && (
          <div className="my-8 flex justify-center">
            <Spinner message="Loading topic..." />
          </div>
        )}
        <div className="my-8 max-w-[700px] mx-auto">
          <TopicDetailsHeader title={topic.title} />
          <TopicDetailsDescription description={topic.description} tags={topic.tags} />
          <hr className="border-gray-800" />
          <TopicDetailsStats />
          <hr className="border-gray-800" />
          <TopicDetailsCommentModal topicId={topic.id} />
          <TopicDetailsComments topicId={topic.id} />
        </div>
      </div>
    </>
  );
};

export default TopicDetails;
