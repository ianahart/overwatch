import { BsThreeDots } from 'react-icons/bs';
import { LuGrip } from 'react-icons/lu';

import { ITodoList } from '../../../../../interfaces';
import AddCard from './AddCard';
import TodoListTitle from './TodoListTitle';

export interface ITodoListProps {
  list: ITodoList;
}

const TodoList = ({ list }: ITodoListProps) => {
  return (
    <div className="bg-slate-900 mx-2 w-full min-w-[225px] max-w-[225px] rounded p-2 text-gray-400">
      <div className="flex items-center justify-between">
        <TodoListTitle list={list} />
        <div>
          <LuGrip className="cursor-pointer" />
        </div>
      </div>
      <div className="flex justify-end">
        <BsThreeDots className="cursor-pointer" />
      </div>
      <AddCard />
    </div>
  );
};

export default TodoList;
