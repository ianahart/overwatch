import { useSelector } from 'react-redux';
import { TRootState } from '../../../../../state/store';
import TodoLists from './TodoLists';

const WorkSpace = () => {
  const { workSpace } = useSelector((store: TRootState) => store.workSpace);

  const style = { backgroundColor: workSpace.backgroundColor ? workSpace.backgroundColor : 'gray' };

  return (
    <div style={style} className="min-h-[700px] w-full rounded p-2 overflow-x-auto">
      {workSpace.id !== 0 && <TodoLists />}
    </div>
  );
};

export default WorkSpace;
