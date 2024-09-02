import { useDispatch, useSelector } from 'react-redux';
import TodoLists from './TodoLists';
import {
  DndContext,
  KeyboardSensor,
  PointerSensor,
  useSensor,
  useSensors,
  closestCorners,
  DragEndEvent,
  UniqueIdentifier,
} from '@dnd-kit/core';
import { arrayMove, sortableKeyboardCoordinates } from '@dnd-kit/sortable';
import { TRootState, setTodoLists, useReorderTodoListsMutation } from '../../../../../state/store';
import { ITodoList } from '../../../../../interfaces';
const WorkSpace = () => {
  const dispatch = useDispatch();
  const [reorderTodoLists] = useReorderTodoListsMutation();
  const { token } = useSelector((store: TRootState) => store.user);
  const { workSpace } = useSelector((store: TRootState) => store.workSpace);
  const { todoLists } = useSelector((store: TRootState) => store.todoList);
  const sensors = useSensors(
    useSensor(PointerSensor, {
      activationConstraint: {
        distance: 5,
      },
    }),
    useSensor(KeyboardSensor, {
      coordinateGetter: sortableKeyboardCoordinates,
    })
  );

  const getTodoListPos = (todoListId: UniqueIdentifier) => {
    const id = Number.parseInt(todoListId.toString());
    return todoLists.findIndex((todoList) => todoList.id === id);
  };

  const handleReorderTodoLists = (newTodoLists: ITodoList[], workSpaceId: number) => {
    reorderTodoLists({ token, todoLists: newTodoLists, workSpaceId })
      .unwrap()
      .then((res) => {
        dispatch(setTodoLists(res.data));
      })
      .catch((err) => {
        console.log(err);
      });
  };

  const handleDragEnd = (e: DragEndEvent) => {
    const { active, over } = e;
    if (!over?.id || active.id === over?.id) return;
    const originalPos = getTodoListPos(active.id);
    const newPos = getTodoListPos(over?.id);

    const newTodoLists = arrayMove(todoLists, originalPos, newPos);
    handleReorderTodoLists(newTodoLists, workSpace.id);
  };

  const style = { backgroundColor: workSpace.backgroundColor ? workSpace.backgroundColor : '#030712' };

  return (
    <div style={style} className="min-h-[700px] w-full rounded p-2 overflow-x-auto">
      {workSpace.id !== 0 && (
        <DndContext sensors={sensors} collisionDetection={closestCorners} onDragEnd={handleDragEnd}>
          <TodoLists />
        </DndContext>
      )}
      {workSpace.id === 0 && (
        <div className="my-4 flex justify-center">
          <h3>Select existing workspace from dropdown or create a new workspace.</h3>
        </div>
      )}
    </div>
  );
};

export default WorkSpace;
