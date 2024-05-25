import { useState } from 'react';
import { IoIosCloseCircleOutline } from 'react-icons/io';
import { useDispatch } from 'react-redux';

import { ISkillsFormField } from '../../../interfaces';
import { addToList, removeFromList } from '../../../state/store';

export interface IBubbleInputListProps {
  data: ISkillsFormField[];
  label: string;
  htmlFor: string;
  id: string;
  listName: string;
}

const BubbleInputList = ({ data, label, htmlFor, id, listName }: IBubbleInputListProps) => {
  const dispatch = useDispatch();
  const [inputValue, setInputValue] = useState('');

  const handleOnChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setInputValue(e.target.value);
  };

  const handleOnKeyDown = (e: React.KeyboardEvent<HTMLInputElement>) => {
    if (e.key.toLowerCase() === 'enter') {
      if (data.length >= 10 || inputValue.trim().length === 0) {
        return;
      }
      dispatch(addToList({ listName, value: inputValue }));
      setInputValue('');
    }
  };

  return (
    <>
      <label htmlFor={htmlFor}>{label}</label>
      <input
        value={inputValue}
        onKeyDown={handleOnKeyDown}
        onChange={handleOnChange}
        name={id}
        id={id}
        className="h-9 rounded bg-transparent border border-gray-800 w-full placeholder:pl-2 pl-2 shadow"
      />
      <div className="p-4 rounded bg-stone-950 my-4">
        <ul className="flex flex-wrap">
          {data.map(({ id, name }) => {
            return (
              <li className="relative m-2 rounded-xl p-2 border border-gray-800 hover:border-green-400 " key={id}>
                <p>{name}</p>
                <IoIosCloseCircleOutline
                  onClick={() => dispatch(removeFromList({ listName, id }))}
                  className="cursor-pointer text-gray-400 absolute right-0 top-0 text-lg"
                />
              </li>
            );
          })}
        </ul>
      </div>
    </>
  );
};

export default BubbleInputList;
