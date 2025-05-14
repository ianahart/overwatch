import { useState } from 'react';
import { useSelector } from 'react-redux';
import { nanoid } from 'nanoid';
import { useNavigate } from 'react-router-dom';

import { IError, IFormTopicTag } from '../../interfaces';
import CreateCommunityTagItem from './CreateCommunityTagItem';
import { TRootState, useCreateTopicMutation } from '../../state/store';

const CreateCommunity = () => {
  const { user, token } = useSelector((store: TRootState) => store.user);
  const [createTopicMut] = useCreateTopicMutation();
  const navigate = useNavigate();
  const MAX_TAGS = 5;
  const MAX_TAG_LENGTH = 50;
  const [error, setError] = useState('');
  const [serverErrors, setServerErrors] = useState<string[]>([]);
  const [title, setTitle] = useState('');
  const [currentTag, setCurrentTag] = useState('');
  const [description, setDescription] = useState('');
  const [tags, setTags] = useState<IFormTopicTag[]>([]);

  const handleOnAddTag = (): void => {
    if (tags.length === MAX_TAGS) {
      setError(`You have already added the max (${MAX_TAGS}) number of tags`);
      return;
    }

    if (currentTag.trim().length === 0 || currentTag.length > MAX_TAG_LENGTH) {
      setError('A tag cannot be empty and must be under 50 characters');
      return;
    }

    const tagExists = tags.find((tag) => tag.name.toLowerCase() === currentTag.toLowerCase());

    if (tagExists) {
      setError(`You already added the tag ${currentTag}`);
      return;
    }

    setTags((prevState) => [...prevState, { id: nanoid(), name: currentTag }]);
    setCurrentTag('');
  };

  const removeTag = (id: string): void => {
    if (!id) return;
    setTags((prevState) => prevState.filter((tag) => tag.id !== id));
  };

  const applyServerErrors = <T extends IError>(errors: T) => {
    for (let prop in errors) {
      setServerErrors((prevState) => [...prevState, errors[prop]]);
    }
  };

  const createTopic = () => {
    const tagNames = tags.map((tag) => tag.name);
    const payload = { title, description, tags: tagNames, userId: user.id, token };
    createTopicMut(payload)
      .unwrap()
      .then(() => {
        navigate('/community');
      })
      .catch((err) => {
        applyServerErrors(err.data);
      });
  };

  const handleOnSubmit = (e: React.FormEvent<HTMLFormElement>): void => {
    e.preventDefault();

    setError('');
    setServerErrors([]);

    if (tags.length === 0) {
      setError('Please add at least one tag for this topic');
      return;
    }

    if ([title, description].some((field) => field.trim().length === 0)) {
      setError('Please make sure to fill out all fields');
      return;
    }

    createTopic();
  };

  return (
    <div className="max-w-[1280px] mx-auto p-2">
      <header className="text-gray-400 flex justify-center my-4">
        <h2 className="text-3xl">Create a community topic</h2>
      </header>
      <div className="border border-gray-800 p-2 rounded max-w-[550px] mx-auto">
        <form onSubmit={handleOnSubmit}>
          {error.length > 0 && (
            <div className="my-2 flex justify-center">
              <p className="text-red-300 text-sm">{error}</p>
            </div>
          )}
          <div className="my-8">
            <label htmlFor="title">Title</label>
            <input
              value={title}
              onChange={(e) => setTitle(e.target.value)}
              placeholder="Topic title..."
              className="w-full border border-gray-800 rounded p-2 bg-transparent"
              name="title"
              id="title"
              type="text"
            />
          </div>
          <div className="my-8">
            <label htmlFor="description">Description</label>
            <textarea
              value={description}
              onChange={(e) => setDescription(e.target.value)}
              className="h-28 resize-none bg-transparent border border-gray-800 rounded w-full"
              id="description"
              name="description"
            ></textarea>
          </div>
          <div className="my-8">
            <label htmlFor="tag">Tags</label>
            <div className="flex items-center">
              <input
                value={currentTag}
                onChange={(e) => setCurrentTag(e.target.value)}
                placeholder="Add tags..."
                className="border border-gray-800 rounded h-9 bg-transparent md:w-[80%] w-[50%]"
                id="tag"
                name="tag"
                type="text"
              />
              <button data-testid="add-tag-btn" type="button" onClick={handleOnAddTag} className="btn ml-2">
                Add tag
              </button>
            </div>
            <div className="my-4 flex-wrap flex">
              {tags.map((tag) => {
                return <CreateCommunityTagItem removeTag={removeTag} key={tag.id} tag={tag} />;
              })}
            </div>
          </div>
          {serverErrors.length > 0 && (
            <div className="my-4 flex flex-col items-center">
              {serverErrors.map((serverError) => {
                return (
                  <p key={nanoid()} className="text-sm text-red-300">
                    {serverError}
                  </p>
                );
              })}
            </div>
          )}
          <div className="my-4 flex justify-center">
            <button type="submit" className="btn w-full">
              Create
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default CreateCommunity;
