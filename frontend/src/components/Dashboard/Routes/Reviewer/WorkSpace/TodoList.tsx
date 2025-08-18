import { BsThreeDots } from 'react-icons/bs';
import { LuGrip } from 'react-icons/lu';
import { SortableContext, useSortable, verticalListSortingStrategy } from '@dnd-kit/sortable';
import { CSS } from '@dnd-kit/utilities';

import { useState } from 'react';

import { ITodoList } from '../../../../../interfaces';
import AddCard from './AddCard';
import TodoListTitle from './TodoListTitle';
import ClickAway from '../../../../Shared/ClickAway';
import TodoListOptions from './TodoListOptions';
import CardList from './CardList';

export interface ITodoListProps {
  list: ITodoList;
}

const TodoList = ({ list }: ITodoListProps) => {
  const { attributes, listeners, setNodeRef, transform, transition } = useSortable({ id: `list-${list.id}` });
  const [isOpen, setIsOpen] = useState(false);

  const onClickAway = () => {
    setIsOpen(false);
  };

  const onClickOpen = () => {
    setIsOpen(true);
  };

  const style = {
    transition,
    transform: CSS.Translate.toString(transform),
  };

  return (
    <div
      data-testid="TodoList"
      ref={setNodeRef}
      style={style}
      {...attributes}
      {...listeners}
      onClick={(e) => e.stopPropagation()}
      className="bg-slate-900 mx-2 w-full min-h-[500px] mb-auto min-w-[225px] max-w-[225px] rounded p-2 text-gray-400"
    >
      <div className="flex items-center justify-between">
        <TodoListTitle list={list} />
        <div>
          <LuGrip className="cursor-pointer" />
        </div>
      </div>
      <div className="flex justify-end relative">
        <BsThreeDots data-testid="todolist-options-trigger" onClick={onClickOpen} className="cursor-pointer" />
        {isOpen && (
          <ClickAway onClickAway={onClickAway}>
            <TodoListOptions onClickClose={onClickAway} todoListId={list.id} />
          </ClickAway>
        )}
      </div>
      <AddCard todoList={list} />
      <div className="my-4">
        <SortableContext
          key={`list-${list.id.toString()}`}
          id={`list-${list.id.toString()}`}
          items={list.cards.map((card) => `card-${card.id}`)}
          strategy={verticalListSortingStrategy}
        >
          {list.cards.length === 0 ? <p className="text-sm">Start a list</p> : <CardList cards={list.cards} />}
        </SortableContext>
      </div>
    </div>
  );
};

export default TodoList;
