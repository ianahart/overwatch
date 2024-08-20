import { BsThreeDots, BsTrash } from 'react-icons/bs';

const WorkSpaceTitle = () => {
  return (
    <div className="flex justify-between bg-slate-900 py-4 px-2 rounded mb-2">
      <p>input</p>
      <div className="flex items-center">
        <div className="mx-2">
          <BsTrash className="text-gray-400" />
        </div>
        <div className="mx-2">
          <BsThreeDots className="text-gray-400" />
        </div>
      </div>
    </div>
  );
};

export default WorkSpaceTitle;
