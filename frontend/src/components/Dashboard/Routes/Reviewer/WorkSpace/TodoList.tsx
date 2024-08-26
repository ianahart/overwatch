import { BsThreeDots } from 'react-icons/bs';
import { LuGrip } from 'react-icons/lu';
import { useSortable } from '@dnd-kit/sortable';
import { CSS } from '@dnd-kit/utilities';

import { ITodoList } from '../../../../../interfaces';
import AddCard from './AddCard';
import TodoListTitle from './TodoListTitle';
import { useState } from 'react';
import ClickAway from '../../../../Shared/ClickAway';
import TodoListOptions from './TodoListOptions';

export interface ITodoListProps {
  list: ITodoList;
}

const TodoList = ({ list }: ITodoListProps) => {
  const { attributes, listeners, setNodeRef, transform, transition } = useSortable({ id: list.id });
  const [isOpen, setIsOpen] = useState(false);

  const onClickAway = () => {
    setIsOpen(false);
  };

  const onClickOpen = () => {
    setIsOpen(true);
  };

  const style = {
    transition,
    transform: CSS.Transform.toString(transform),
  };

  return (
    <div
      ref={setNodeRef}
      style={style}
      {...attributes}
      {...listeners}
      onClick={(e) => e.stopPropagation()}
      className="bg-slate-900 mx-2 w-full min-w-[225px] max-w-[225px] rounded p-2 text-gray-400"
    >
      <div className="flex items-center justify-between">
        <TodoListTitle list={list} />
        <div>
          <LuGrip className="cursor-pointer" />
        </div>
      </div>
      <div className="flex justify-end relative">
        <BsThreeDots onClick={onClickOpen} className="cursor-pointer" />
        {isOpen && (
          <ClickAway onClickAway={onClickAway}>
            <TodoListOptions onClickClose={onClickAway} todoListId={list.id} />
          </ClickAway>
        )}
      </div>
      <AddCard />
    </div>
  );
};

export default TodoList;
