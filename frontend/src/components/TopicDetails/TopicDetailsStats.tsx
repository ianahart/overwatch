import { BiComment } from 'react-icons/bi';
import { FaShare } from 'react-icons/fa';
import { TbArrowBigDown, TbArrowBigUp } from 'react-icons/tb';

const TopicDetailsStats = () => {
  return (
    <div className="my-12 w-full">
      <div className="flex justify-start">
        <div className="m-2 flex items-center bg-gray-800 p-2 rounded-2xl">
          <div className="mx-1 cursor-pointer">
            <TbArrowBigUp className="text-xl" />
          </div>
          <div className="mx-1">
            <p>0</p>
          </div>
          <div className="mx-1 cursor-pointer">
            <TbArrowBigDown className="text-xl" />
          </div>
        </div>
        <div className="m-2 flex items-center bg-gray-800 p-2 rounded-2xl justify-between">
          <div className="mx-1">
            <BiComment className="text-xl" />
          </div>
          <div>
            <p>0</p>
          </div>
        </div>
        <div className="m-2 flex items-center bg-gray-800 p-2 rounded-2xl justify-between cursor-pointer">
          <div className="mx-1">
            <FaShare />
          </div>
          <div>
            <p>Share</p>
          </div>
        </div>
      </div>
    </div>
  );
};

export default TopicDetailsStats;
