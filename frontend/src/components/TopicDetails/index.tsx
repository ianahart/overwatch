import { useParams } from 'react-router-dom';

const TopicDetails = () => {
  const { topicId } = useParams();
  return <div>Topic Id: {topicId}</div>;
};

export default TopicDetails;
