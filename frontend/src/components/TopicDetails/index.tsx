import { useParams } from 'react-router-dom';
import { useEffect, useState } from 'react';
import { useFetchTopicQuery } from '../../state/store';
import { topicState } from '../../data';
import { ITopic } from '../../interfaces';
import Spinner from '../Shared/Spinner';
import TopicDetailsHeader from './TopicDetailsHeader';
import TopicDetailsDescription from './TopicDetailsDescription';

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
      <div className="max-w-[1280px] border mx-auto p-2 min-h-[100vh]">
        {isLoading && (
          <div className="my-8 flex justify-center">
            <Spinner message="Loading topic..." />
          </div>
        )}
        <div className="my-8 flex flex-col items-center">
          <TopicDetailsHeader title={topic.title} />
          <TopicDetailsDescription description={topic.description} tags={topic.tags} />
        </div>
      </div>
    </>
  );
};

export default TopicDetails;
