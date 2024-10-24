import { useParams } from 'react-router-dom';
import { useEffect } from 'react';
import { useFetchTopicQuery } from '../../state/store';

const TopicDetails = () => {
  let { topicId: potentialTopicId } = useParams();
  const topicId = Number.parseInt(potentialTopicId as string);
  const { data, isLoading } = useFetchTopicQuery({ topicId });

  useEffect(() => {
    if (data !== undefined) {
      console.log(data);
    }
  }, [data]);

  return <div>Topic Id: {topicId}</div>;
};

export default TopicDetails;
