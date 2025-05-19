import { useSelector } from 'react-redux';
import { useParams, useNavigate } from 'react-router-dom';
import { useCallback, useEffect, useState } from 'react';
import { topicState } from '../../data';
import { TRootState, useFetchTopicQuery, useUpdateTopicMutation } from '../../state/store';
import { IError, ITopic } from '../../interfaces';
import TagEditItem from './TagEditItem';
import { nanoid } from 'nanoid';

const TopicEdit = () => {
  const MAX_DESCRIPTION_LENGTH = 250;
  const navigate = useNavigate();
  const { topicId } = useParams();
  const parsedTopicId = Number.parseInt(topicId as string);
  const { user, token } = useSelector((store: TRootState) => store.user);
  const [topic, setTopic] = useState<ITopic>(topicState);
  const { data } = useFetchTopicQuery({ topicId: parsedTopicId }, { skip: !parsedTopicId });
  const [updateTopic] = useUpdateTopicMutation();
  const [errors, setErrors] = useState<string[]>([]);

  useEffect(() => {
    if (data !== undefined) {
      setTopic(data.data);
    }
  }, [data]);

  const handleOnChange = (e: React.ChangeEvent<HTMLTextAreaElement>): void => {
    const description = e.target.value;
    setTopic((prevState) => ({
      ...prevState,
      description,
    }));
  };

  const handleUpdateTag = useCallback(
    (name: string, id: number): void => {
      const tags = topic.tags.map((tag) => {
        if (tag.id === id) {
          return { ...tag, name };
        }
        return { ...tag };
      });

      setTopic((prevState) => ({
        ...prevState,
        tags,
      }));
    },
    [topic.tags]
  );

  const handleDeleteTag = (id: number): void => {
    if (topic.tags.length === 1) {
      setErrors((prevState) => [...prevState, 'You must have at least one tag']);
      return;
    }
    const tags = topic.tags.filter((tag) => tag.id !== id);
    setTopic((prevState) => ({
      ...prevState,
      tags,
    }));
  };

  const applyErrors = <T extends IError>(errors: T) => {
    for (let prop in errors) {
      setErrors((prevState) => [...prevState, errors[prop]]);
    }
  };

  const handleOnSubmit = (e: React.FormEvent<HTMLFormElement>): void => {
    e.preventDefault();
    setErrors([]);
    if (topic.description.trim().length > MAX_DESCRIPTION_LENGTH) {
      setErrors((prevState) => [...prevState, `Description must be between 1 and ${MAX_DESCRIPTION_LENGTH}`]);
      return;
    }
    const tags = topic.tags.map((tag) => tag.name);
    const payload = { token, userId: user.id, description: topic.description, topicId: parsedTopicId, tags };

    updateTopic(payload)
      .unwrap()
      .then(() => {
        navigate('/community');
      })
      .catch((err) => {
        applyErrors(err.data);
      });
  };

  return (
    <div className="max-w-[1280px] mx-auto p-2 min-h-[100vh]">
      <div className="max-w-[600px] w-full rouned bg-stone-950 p-2 mx-auto mt-10">
        <h3 className="text-2xl border-gray-800 border rounded w-full p-2">{topic.title}</h3>
        <form onSubmit={handleOnSubmit}>
          {errors.length > 0 && (
            <div>
              {errors.map((error) => {
                return (
                  <p key={nanoid()} className="my-1 text-red-300 text-sm">
                    {error}
                  </p>
                );
              })}
            </div>
          )}
          <div className="my-8">
            <label htmlFor="desc">Description</label>
            <textarea
              data-testid="topic-edit-description"
              onChange={handleOnChange}
              className="resize-none w-full border border-gray-800 rounded p-2 h-20 bg-transparent"
              value={topic.description}
              id="desc"
              name="desc"
            ></textarea>
          </div>
          <div className="my-8">
            {topic.tags.map((tag) => {
              return (
                <TagEditItem
                  handleDeleteTag={handleDeleteTag}
                  handleUpdateTag={handleUpdateTag}
                  key={tag.id}
                  tag={tag}
                />
              );
            })}
          </div>
          <button type="submit" className="btn">
            Update
          </button>
        </form>
      </div>
    </div>
  );
};

export default TopicEdit;
