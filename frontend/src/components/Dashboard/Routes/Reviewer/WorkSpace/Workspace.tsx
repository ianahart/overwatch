import { useDispatch, useSelector } from 'react-redux';
import { DndContext, PointerSensor, useSensor, useSensors, closestCorners, DragEndEvent } from '@dnd-kit/core';
import { SortableContext, arrayMove, horizontalListSortingStrategy } from '@dnd-kit/sortable';

import TodoLists from './TodoLists';
import {
  TRootState,
  moveTodoCard,
  reorderTodoCards,
  setTodoLists,
  useReorderTodoListsMutation,
  useReorderTodoCardsMutation,
  useMoveTodoCardsMutation,
  useFetchLabelsQuery,
  setLabels,
} from '../../../../../state/store';
import { ITodoList } from '../../../../../interfaces';
import { useEffect } from 'react';
const WorkSpace = () => {
  const dispatch = useDispatch();
  const [reorderTodoLists] = useReorderTodoListsMutation();
  const [reorderTodoCardsMut] = useReorderTodoCardsMutation();
  const [moveTodoCardsMut] = useMoveTodoCardsMutation();
  const { token } = useSelector((store: TRootState) => store.user);
  const { workSpace } = useSelector((store: TRootState) => store.workSpace);
  const { todoLists } = useSelector((store: TRootState) => store.todoList);
  const { data } = useFetchLabelsQuery({ token, workSpaceId: workSpace.id });

  useEffect(() => {
    if (data !== undefined) {
      dispatch(setLabels(data.data));
    }
  }, [data]);

  const sensors = useSensors(
    useSensor(PointerSensor, {
      activationConstraint: {
        distance: 5,
      },
    })
  );

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

  const handleReorderTodoCards = (listId: number, oldIndex: number, newIndex: number, todoCardId: number) => {
    reorderTodoCardsMut({ token, listId, oldIndex, newIndex, todoCardId })
      .unwrap()
      .then(() => {
        dispatch(reorderTodoCards({ listId, oldIndex, newIndex }));
      })
      .catch((err) => {
        console.log(err);
      });
  };

  const handleMoveTodoCards = (
    sourceListId: number,
    destinationListId: number,
    todoCardId: number,
    newIndex: number
  ) => {
    moveTodoCardsMut({ token, sourceListId, destinationListId, todoCardId, newIndex })
      .unwrap()
      .then((res) => {
        console.log(res);
        dispatch(
          moveTodoCard({
            sourceListId,
            destinationListId,
            cardId: todoCardId,
            newIndex,
          })
        );
      })
      .catch((err) => {
        console.log(err);
      });
  };

  const handleDragEnd = (e: DragEndEvent) => {
    const { active, over } = e;

    if (!active || !over) return;

    const activeId = active.id.toString();
    const overId = over.id.toString();

    const isList = activeId.startsWith('list-');
    const isCard = activeId.startsWith('card-');

    if (isList) {
      const oldIndex = todoLists.findIndex((tl) => `list-${tl.id}` === activeId);
      const newIndex = todoLists.findIndex((tl) => `list-${tl.id}` === overId);

      if (newIndex !== -1) {
        const newTodoLists = arrayMove(todoLists, oldIndex, newIndex);
        handleReorderTodoLists(newTodoLists, workSpace.id);
      }
    } else if (isCard) {
      const sourceList = todoLists.find((list) => list.cards.some((card) => `card-${card.id}` === activeId));
      const destinationList = todoLists.find((list) => list.cards.some((card) => `card-${card.id}` === overId));
      const cardId = Number.parseInt(activeId.replace('card-', ''));

      if (!destinationList && sourceList) {
        const destinationListId = Number.parseInt(overId.replace('list-', ''));
        handleMoveTodoCards(sourceList.id, destinationListId, cardId, 0);
        return;
      }

      if (sourceList && destinationList) {
        const oldIndex = sourceList.cards.findIndex((card) => `card-${card.id}` === activeId);
        const newIndex = destinationList.cards.findIndex((card) => `card-${card.id}` === overId);

        if (sourceList.id === destinationList.id) {
          handleReorderTodoCards(sourceList.id, oldIndex, newIndex, cardId);
        } else {
          handleMoveTodoCards(sourceList.id, destinationList.id, cardId, newIndex);
        }
      }
    }
  };

  const style = { backgroundColor: workSpace.backgroundColor ? workSpace.backgroundColor : '#030712' };

  return (
    <div style={style} className="min-h-[700px] w-full rounded p-2 overflow-x-auto">
      {workSpace.id !== 0 && (
        <DndContext sensors={sensors} collisionDetection={closestCorners} onDragEnd={handleDragEnd}>
          <SortableContext
            id="todo-lists"
            items={todoLists.map((list) => `list-${list.id}`)}
            strategy={horizontalListSortingStrategy}
          >
            <TodoLists />
          </SortableContext>
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
