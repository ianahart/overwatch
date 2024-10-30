import { BiComment } from 'react-icons/bi';
import { FaShare } from 'react-icons/fa';
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

export interface ITopicDetailsStatsProps {
  totalCommentCount: number;
}

const TopicDetailsStats = ({ totalCommentCount }: ITopicDetailsStatsProps) => {
  const handleCopyUrl = async () => {
    try {
      const currentUrl = window.location.href;
      await navigator.clipboard.writeText(currentUrl);
      initiateToast();
    } catch (err) {
      console.error(err);
    }
  };

  const initiateToast = () => {
    toast.success('The current url has been copied to your clipboard!', {
      position: 'bottom-center',
      autoClose: 5000,
      hideProgressBar: false,
      closeOnClick: true,
      pauseOnHover: true,
      draggable: true,
      progress: undefined,
      theme: 'dark',
    });
  };

  return (
    <div className="my-12 w-full">
      <div className="flex justify-start">
        <div className="m-2 flex items-center bg-gray-800 p-2 rounded-2xl justify-between">
          <div className="mx-1">
            <BiComment className="text-xl" />
          </div>
          <div>
            <p>{totalCommentCount}</p>
          </div>
        </div>
        <div
          onClick={handleCopyUrl}
          className="m-2 flex items-center bg-gray-800 p-2 rounded-2xl justify-between cursor-pointer"
        >
          <div className="mx-1">
            <FaShare />
          </div>
          <div>
            <p>Share</p>
          </div>
        </div>
      </div>
      <ToastContainer />
    </div>
  );
};

export default TopicDetailsStats;
