import { useSelector } from 'react-redux';
import { TRootState } from '../../../../../state/store';

const WorkSpace = () => {
  const { workSpace } = useSelector((store: TRootState) => store.workSpace);

  const style = { backgroundColor: workSpace.backgroundColor ? workSpace.backgroundColor : 'gray' };

  return <div style={style} className="w-full min-h-[700px] bg-blue-50"></div>;
};

export default WorkSpace;
