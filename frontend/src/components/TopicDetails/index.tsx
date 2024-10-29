import { useNavigate, useParams } from 'react-router-dom';
import { AiOutlinePlus } from 'react-icons/ai';
import { useSelector } from 'react-redux';
import { useEffect, useState } from 'react';

import { TRootState, useFetchTopicQuery } from '../../state/store';
import { topicState } from '../../data';
import { ITopic } from '../../interfaces';
import Spinner from '../Shared/Spinner';
import TopicDetailsHeader from './TopicDetailsHeader';
import TopicDetailsDescription from './TopicDetailsDescription';
import TopicDetailsStats from './TopicDetailsStats';
import TopicDetailsCommentModalContent from './TopicDetailsCommentModalContent';
import TopicDetailsComments from './TopicDetailsComments';
import TopicDetailsModal from './TopicDetailsModal';

const TopicDetails = () => {
  const navigate = useNavigate();
  const { user } = useSelector((store: TRootState) => store.user);
  let { topicId: potentialTopicId } = useParams();
  const topicId = Number.parseInt(potentialTopicId as string);
  const { data, isLoading } = useFetchTopicQuery({ topicId });
  const [topic, setTopic] = useState<ITopic>(topicState);
  const [modalOpen, setModalOpen] = useState(false);

  useEffect(() => {
    if (data !== undefined) {
      setTopic(data.data);
    }
  }, [data]);

  const handleCloseModal = (): void => setModalOpen(false);

  const handleOpenModal = (): void => {
    if (user.id === 0 || !user.id) {
      navigate('/signin');
      return;
    }
    setModalOpen(true);
  };

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
          <TopicDetailsStats totalCommentCount={topic.totalCommentCount} />
          <hr className="border-gray-800" />
          <div>
            <button
              onClick={handleOpenModal}
              className="flex items-center p-2 border-gray-800 border rounded-xl cursor-pointer"
            >
              <AiOutlinePlus className="mr-2 text-xl" />
              Add a comment
            </button>
          </div>
          {modalOpen && (
            <TopicDetailsModal closeModal={handleCloseModal}>
              <TopicDetailsCommentModalContent handleCloseModal={handleCloseModal} topicId={topic.id} />
            </TopicDetailsModal>
          )}
          <TopicDetailsComments topicId={topic.id} />
        </div>
      </div>
    </>
  );
};

export default TopicDetails;
