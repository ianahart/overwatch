import { useState } from 'react';
import { AiOutlineClose } from 'react-icons/ai';
import { useDispatch, useSelector } from 'react-redux';
import Header from '../Header';
import { TRootState, addWorkExpToList, removeWorkExpFromList } from '../../../state/store';

export type TFormElement = HTMLInputElement | HTMLTextAreaElement;

const WorkExperience = () => {
  const dispatch = useDispatch();
  const { workExps } = useSelector((store: TRootState) => store.workExp);
  const [title, setTitle] = useState('');
  const [desc, setDesc] = useState('');

  const handleOnChange = (e: React.ChangeEvent<TFormElement>) => {
    const { name, value } = e.target;
    name === 'desc' ? setDesc(value) : setTitle(value);
  };

  const handleOnClick = () => {
    const emptyFields = title.trim().length === 0 || desc.trim().length === 0;

    if (workExps.length >= 10 || emptyFields) {
      return;
    }

    dispatch(addWorkExpToList({ title, desc }));
    setTitle('');
    setDesc('');
  };

  return (
    <div>
      <Header heading="Work & Experience" />
      <div className="my-4">
        <label htmlFor="title">Title (ex. Software Engineer)</label>
        <input
          value={title}
          onChange={handleOnChange}
          name="title"
          id="title"
          className="h-9 rounded bg-transparent border border-gray-800 w-full placeholder:pl-2 pl-2 shadow"
        />
      </div>
      <div className="my-4">
        <label htmlFor="desc">Description</label>
        <textarea
          value={desc}
          onChange={handleOnChange}
          className="h-24 rounded bg-transparent border border-gray-800 w-full placeholder:pl-2 pl-2 shadow"
          id="desc"
          name="desc"
        ></textarea>
      </div>
      <div className="my-4 flex justify-start">
        <button onClick={handleOnClick} type="button" className="btn w-20">
          Add
        </button>
      </div>
      {workExps.length > 0 && (
        <div className="p-4 rounded bg-stone-950 my-4">
          <ul>
            {workExps.map(({ id, title, desc }) => {
              return (
                <li key={id} className=" my-2">
                  <div className="flex justify-between items-center">
                    <h3 className="text-xl text-gray-400">{title}</h3>
                    <AiOutlineClose
                      data-testid="ai-outline-close"
                      onClick={() => dispatch(removeWorkExpFromList(id))}
                      className="cursor-pointer"
                    />
                  </div>
                  <p>{desc}</p>
                </li>
              );
            })}
          </ul>
        </div>
      )}
    </div>
  );
};

export default WorkExperience;
