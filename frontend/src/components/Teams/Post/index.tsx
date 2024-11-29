import { useState } from 'react';
import CreatePostModal from './CreatePostModal';
import CodeEditor from './CodeEditor';
import TeamPostList from './TeamPostList';
const Post = () => {
  const [isModalOpen, setIsModalOpen] = useState(false);

  const handleCloseModal = (): void => {
    setIsModalOpen(false);
  };

  const handleOpenModal = (): void => {
    setIsModalOpen(true);
  };
  return (
    <div className="bg-black min-h-[100vh] text-gray-400">
      <div className="flex flex-col items-center max-w-[750px] w-full mx-auto">
        <div className="border border-gray-800 rounded w-full p-2 mt-10">
          <div>
            <button onClick={handleOpenModal} className="bg-blue-400 p-1 h-9 rounded text-gray-800 w-full">
              Create post
            </button>
          </div>
          <div className="my-8">
            <TeamPostList />
          </div>
          {isModalOpen && (
            <CreatePostModal closeModal={handleCloseModal}>
              <CodeEditor closeModal={handleCloseModal} />
            </CreatePostModal>
          )}
        </div>
      </div>
    </div>
  );
};

export default Post;
