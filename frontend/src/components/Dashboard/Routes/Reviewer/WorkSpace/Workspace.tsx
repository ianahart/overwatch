import { useSelector } from 'react-redux';
import { TRootState } from '../../../../../state/store';
import AddTodoList from './AddTodoList';

const WorkSpace = () => {
  const { workSpace } = useSelector((store: TRootState) => store.workSpace);

  const style = { backgroundColor: workSpace.backgroundColor ? workSpace.backgroundColor : 'gray' };

  return (
    <div style={style} className="w-full min-h-[700px] rounded p-2">
      {workSpace.id !== 0 && <AddTodoList />}
    </div>
  );
};

export default WorkSpace;
