import { useNavigate, useParams } from 'react-router-dom';
import { useEffect, useState } from 'react';
import { useSelector } from 'react-redux';

import { retrieveTokens } from '../../../../../util';
import {
  TRootState,
  useFetchUserCommentRepositoryQuery,
  useUpdateRepositoryCommentMutation,
} from '../../../../../state/store';

const EditReview = () => {
  const { user } = useSelector((store: TRootState) => store.user);
  const token = retrieveTokens()?.token;
  const navigate = useNavigate();
  const { repositoryId } = useParams();
  const { data } = useFetchUserCommentRepositoryQuery({ token, repositoryId: Number.parseInt(repositoryId as string) });
  const [updateRepositoryComment] = useUpdateRepositoryCommentMutation();
  const [comment, setComment] = useState('');

  useEffect(() => {
    if (data !== undefined) {
      console.log(data.data);
      setComment(data.data);
    }
  }, [data]);

  const handleOnSubmit = async (e: React.ChangeEvent<HTMLFormElement>) => {
    try {
      e.preventDefault();

      await updateRepositoryComment({ token, repositoryId: Number.parseInt(repositoryId as string), comment }).unwrap();
      navigate(`/dashboard/${user.slug}/user/reviews`);
    } catch (err) {
      throw new Error(err as string);
    }
  };

  return (
    <div className="flex flex-col justify-center items-center">
      <div className="max-w-[550px] w-full p-2 bg-gray-900 rounded">
        <form onSubmit={handleOnSubmit}>
          <div className="flex flex-col items-center justify-center">
            <h3 className="text-gray-400 text-xl">Edit Repository Comment</h3>
            <p className="text-xs">This is the comment(s) you left for the reviewer to go along with your code.</p>
          </div>
          <div className="my-4 flex flex-col">
            <label className="mb-2" htmlFor="comment">
              Repository comment
            </label>
            <textarea
              onChange={(e) => setComment(e.target.value)}
              value={comment}
              id="comment"
              name="comment"
              className="bg-transparent border border-gray-800 rounded p-1 min-h-24"
            ></textarea>
          </div>
          <div className="flex justify-center my-4">
            <button type="submit" className="btn">
              Update repository comment
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default EditReview;
